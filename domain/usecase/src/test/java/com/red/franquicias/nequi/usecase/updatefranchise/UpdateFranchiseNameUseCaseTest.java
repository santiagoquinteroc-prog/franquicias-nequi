package com.red.franquicias.nequi.usecase.updatefranchise;

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
class UpdateFranchiseNameUseCaseTest {

    @Mock
    private FranchiseRepository franchiseRepository;

    private UpdateFranchiseNameUseCase useCase;

    private Franchise existingFranchise;

    @BeforeEach
    void setUp() {
        useCase = new UpdateFranchiseNameUseCase(franchiseRepository);
        existingFranchise = new Franchise(1L, "Original Name");
    }

    @Test
    void updateName_validName_shouldReturnUpdatedFranchise() {
        Franchise updatedFranchise = new Franchise(1L, "New Name");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.existsByNameFranchise("New Name")).thenReturn(Mono.just(false));
        when(franchiseRepository.saveFranchise(any(Franchise.class))).thenReturn(Mono.just(updatedFranchise));

        StepVerifier.create(useCase.updateName(1L, "New Name"))
                .expectNext(updatedFranchise)
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(franchiseRepository).existsByNameFranchise("New Name");
        verify(franchiseRepository).saveFranchise(any(Franchise.class));
    }

    @Test
    void updateName_franchiseNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(999L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateName(999L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(999L);
        verify(franchiseRepository, never()).existsByNameFranchise(any());
        verify(franchiseRepository, never()).saveFranchise(any());
    }

    @Test
    void updateName_duplicateName_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.existsByNameFranchise("Duplicate Name")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.updateName(1L, "Duplicate Name"))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(franchiseRepository).existsByNameFranchise("Duplicate Name");
        verify(franchiseRepository, never()).saveFranchise(any());
    }

    @Test
    void updateName_sameName_shouldReturnUpdatedFranchise() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.existsByNameFranchise("Original Name")).thenReturn(Mono.just(true));
        when(franchiseRepository.saveFranchise(any(Franchise.class))).thenReturn(Mono.just(existingFranchise));

        StepVerifier.create(useCase.updateName(1L, "Original Name"))
                .expectNext(existingFranchise)
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(franchiseRepository).existsByNameFranchise("Original Name");
        verify(franchiseRepository).saveFranchise(any(Franchise.class));
    }

    @Test
    void updateName_repositoryFindError_shouldReturnTechnicalException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateName(1L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.FRANCHISE_UPDATE_NAME_ERROR
                        && te.getCause() == repositoryError
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(franchiseRepository, never()).existsByNameFranchise(any());
        verify(franchiseRepository, never()).saveFranchise(any());
    }

    @Test
    void updateName_repositoryExistsError_shouldReturnTechnicalException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.existsByNameFranchise("New Name")).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateName(1L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.FRANCHISE_UPDATE_NAME_ERROR
                        && te.getCause() == repositoryError
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(franchiseRepository).existsByNameFranchise("New Name");
        verify(franchiseRepository, never()).saveFranchise(any());
    }

    @Test
    void updateName_repositorySaveError_shouldReturnTechnicalException() {
        RuntimeException repositoryError = new RuntimeException("Database save error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.existsByNameFranchise("New Name")).thenReturn(Mono.just(false));
        when(franchiseRepository.saveFranchise(any(Franchise.class))).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateName(1L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.FRANCHISE_UPDATE_NAME_ERROR
                        && te.getCause() == repositoryError
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(franchiseRepository).existsByNameFranchise("New Name");
        verify(franchiseRepository).saveFranchise(any(Franchise.class));
    }

    @Test
    void updateName_businessExceptionNotWrapped_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(franchiseRepository.existsByNameFranchise("Duplicate")).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.updateName(1L, "Duplicate"))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS
                )
                .verify();
    }
}