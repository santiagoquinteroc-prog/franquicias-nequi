package com.red.franquicias.nequi.usecase.createbranch;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.branch.Branch;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateBranchNameUseCaseTest {

    @Mock
    BranchRepository branchRepository;

    @InjectMocks
    CreateBranchNameUseCase useCase;

    private Branch validBranch;

    @BeforeEach
    void setUp() {
        validBranch = new Branch(null, 1L, "Test Branch");
    }

    @Test
    void create_whenFranchiseNotFound_shouldEmitBusinessException() {
        when(branchRepository.findByIdBranch(1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.create(validBranch))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be &&
                        be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NOT_FOUND
                )
                .verify();

        verify(branchRepository).findByIdBranch(1L);
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void create_whenDuplicateName_shouldEmitBusinessException() {
        when(branchRepository.findByIdBranch(1L)).thenReturn(Mono.just(new Branch(99L, 1L, "any")));
        when(branchRepository.existsByNameAndFranchiseId("Test Branch", 1L)).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.create(validBranch))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be &&
                        be.getTechnicalMessage() == TechnicalMessage.BRANCH_NAME_ALREADY_EXISTS
                )
                .verify();

        verify(branchRepository).findByIdBranch(1L);
        verify(branchRepository).existsByNameAndFranchiseId("Test Branch", 1L);
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void create_whenValid_shouldSaveAndReturnBranch() {
        Branch saved = new Branch(10L, 1L, "Test Branch");

        when(branchRepository.findByIdBranch(1L)).thenReturn(Mono.just(new Branch(99L, 1L, "any")));
        when(branchRepository.existsByNameAndFranchiseId("Test Branch", 1L)).thenReturn(Mono.just(false));
        when(branchRepository.saveBranch(any(Branch.class))).thenReturn(Mono.just(saved));

        StepVerifier.create(useCase.create(validBranch))
                .expectNext(saved)
                .verifyComplete();

        verify(branchRepository).findByIdBranch(1L);
        verify(branchRepository).existsByNameAndFranchiseId("Test Branch", 1L);
        verify(branchRepository).saveBranch(validBranch);
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void create_whenRepositoryFails_shouldWrapAsTechnicalException() {
        when(branchRepository.findByIdBranch(1L))
                .thenReturn(Mono.error(new RuntimeException("DB down")));

        StepVerifier.create(useCase.create(validBranch))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te &&
                        te.getTechnicalMessage() == TechnicalMessage.BRANCH_CREATE_ERROR
                )
                .verify();

        verify(branchRepository).findByIdBranch(1L);
        verifyNoMoreInteractions(branchRepository);
    }

    @Test
    void create_whenNullBranch_shouldThrowNpe_immediately() {
        StepVerifier.create(Mono.fromCallable(() -> useCase.create(null)).flatMap(m -> m))
                .expectError(NullPointerException.class)
                .verify();
    }
}
