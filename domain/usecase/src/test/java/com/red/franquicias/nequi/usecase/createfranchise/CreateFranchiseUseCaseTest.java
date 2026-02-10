package com.red.franquicias.nequi.usecase.createfranchise;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.franchise.Franchise;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateFranchiseUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    private CreateFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateFranchiseUseCase(franchiseRepository);
    }

    @Test
    void create_duplicateName_shouldReturnBusinessException() {
        Franchise franchise = new Franchise(null, "Test Franchise");
        when(franchiseRepository.existsByNameFranchise("Test Franchise"))
                .thenReturn(Mono.just(true));

        StepVerifier.create(useCase.create(franchise))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS
                )
                .verify();

        verify(franchiseRepository).existsByNameFranchise("Test Franchise");
        verify(franchiseRepository, never()).saveFranchise(any());
    }

    @Test
    void create_valid_shouldSaveSuccessfully() {

        Franchise inputFranchise = new Franchise(null, "Test Franchise");
        Franchise savedFranchise = new Franchise(1L, "Test Franchise");

        when(franchiseRepository.existsByNameFranchise("Test Franchise"))
                .thenReturn(Mono.just(false));
        when(franchiseRepository.saveFranchise(inputFranchise))
                .thenReturn(Mono.just(savedFranchise));


        StepVerifier.create(useCase.create(inputFranchise))
                .expectNext(savedFranchise)
                .verifyComplete();

        verify(franchiseRepository).existsByNameFranchise("Test Franchise");
        verify(franchiseRepository).saveFranchise(inputFranchise);
    }

    @Test
    void create_repositoryExistsError_shouldReturnTechnicalException() {

        Franchise franchise = new Franchise(null, "Test Franchise");
        RuntimeException repositoryError = new RuntimeException("Database connection error");

        when(franchiseRepository.existsByNameFranchise("Test Franchise"))
                .thenReturn(Mono.error(repositoryError));


        StepVerifier.create(useCase.create(franchise))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.FRANCHISE_CREATE_ERROR
                        && te.getCause() == repositoryError
                )
                .verify();

        verify(franchiseRepository).existsByNameFranchise("Test Franchise");
        verify(franchiseRepository, never()).saveFranchise(any());
    }

    @Test
    void create_repositorySaveError_shouldReturnTechnicalException() {

        Franchise franchise = new Franchise(null, "Test Franchise");
        RuntimeException repositoryError = new RuntimeException("Database save error");

        when(franchiseRepository.existsByNameFranchise("Test Franchise"))
                .thenReturn(Mono.just(false));
        when(franchiseRepository.saveFranchise(franchise))
                .thenReturn(Mono.error(repositoryError));


        StepVerifier.create(useCase.create(franchise))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.FRANCHISE_CREATE_ERROR
                        && te.getCause() == repositoryError
                )
                .verify();

        verify(franchiseRepository).existsByNameFranchise("Test Franchise");
        verify(franchiseRepository).saveFranchise(franchise);
    }

    @Test
    void create_businessExceptionDuringProcess_shouldNotWrapInTechnicalException() {

        Franchise franchise = new Franchise(null, "Test Franchise");
        BusinessException businessException = new BusinessException(
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS
        );

        when(franchiseRepository.existsByNameFranchise("Test Franchise"))
                .thenReturn(Mono.just(true));


        StepVerifier.create(useCase.create(franchise))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be == businessException.getClass().cast(ex)
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS
                )
                .verify();
    }
}