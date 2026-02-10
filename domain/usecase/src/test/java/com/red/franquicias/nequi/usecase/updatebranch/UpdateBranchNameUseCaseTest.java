package com.red.franquicias.nequi.usecase.updatebranch;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.branch.Branch;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
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
class UpdateBranchNameUseCaseTest {

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    private UpdateBranchNameUseCase useCase;

    private Franchise existingFranchise;
    private Branch existingBranch;

    @BeforeEach
    void setUp() {
        useCase = new UpdateBranchNameUseCase(branchRepository, franchiseRepository);
        existingFranchise = new Franchise(1L, "Test Franchise");
        existingBranch = new Branch(1L, 1L, "Original Name");
    }

    @Test
    void updateName_validName_shouldReturnUpdatedBranch() {
        Branch updatedBranch = new Branch(1L, 1L, "New Name");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(branchRepository.existsByNameAndFranchiseId("New Name", 1L)).thenReturn(Mono.just(false));
        when(branchRepository.saveBranch(any(Branch.class))).thenReturn(Mono.just(updatedBranch));

        StepVerifier.create(useCase.updateName(1L, 1L, "New Name"))
                .expectNext(updatedBranch)
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(branchRepository).existsByNameAndFranchiseId("New Name", 1L);
        verify(branchRepository).saveBranch(any(Branch.class));
    }

    @Test
    void updateName_franchiseNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(999L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateName(1L, 999L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(999L);
        verify(branchRepository, never()).findByIdAndFranchiseId(any(), any());
        verify(branchRepository, never()).existsByNameAndFranchiseId(any(), any());
        verify(branchRepository, never()).saveBranch(any());
    }

    @Test
    void updateName_branchNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(999L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateName(999L, 1L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.BRANCH_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(999L, 1L);
        verify(branchRepository, never()).existsByNameAndFranchiseId(any(), any());
        verify(branchRepository, never()).saveBranch(any());
    }

    @Test
    void updateName_branchNotBelongsToFranchise_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateName(1L, 1L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.BRANCH_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(branchRepository, never()).existsByNameAndFranchiseId(any(), any());
        verify(branchRepository, never()).saveBranch(any());
    }

    @Test
    void updateName_duplicateName_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(branchRepository.existsByNameAndFranchiseId("Duplicate Name", 1L)).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.updateName(1L, 1L, "Duplicate Name"))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.BRANCH_NAME_ALREADY_EXISTS
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(branchRepository).existsByNameAndFranchiseId("Duplicate Name", 1L);
        verify(branchRepository, never()).saveBranch(any());
    }

    @Test
    void updateName_sameName_shouldReturnUpdatedBranch() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(branchRepository.existsByNameAndFranchiseId("Original Name", 1L)).thenReturn(Mono.just(true));
        when(branchRepository.saveBranch(any(Branch.class))).thenReturn(Mono.just(existingBranch));

        StepVerifier.create(useCase.updateName(1L, 1L, "Original Name"))
                .expectNext(existingBranch)
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(branchRepository).existsByNameAndFranchiseId("Original Name", 1L);
        verify(branchRepository).saveBranch(any(Branch.class));
    }

    @Test
    void updateName_franchiseRepositoryError_shouldReturnTechnicalException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateName(1L, 1L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.BRANCH_UPDATE_ERROR
                        && te.getCause() == repositoryError
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository, never()).findByIdAndFranchiseId(any(), any());
        verify(branchRepository, never()).existsByNameAndFranchiseId(any(), any());
        verify(branchRepository, never()).saveBranch(any());
    }

    @Test
    void updateName_branchFindRepositoryError_shouldReturnTechnicalException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateName(1L, 1L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.BRANCH_UPDATE_ERROR
                        && te.getCause() == repositoryError
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(branchRepository, never()).existsByNameAndFranchiseId(any(), any());
        verify(branchRepository, never()).saveBranch(any());
    }

    @Test
    void updateName_branchExistsRepositoryError_shouldReturnTechnicalException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(branchRepository.existsByNameAndFranchiseId("New Name", 1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateName(1L, 1L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.BRANCH_UPDATE_ERROR
                        && te.getCause() == repositoryError
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(branchRepository).existsByNameAndFranchiseId("New Name", 1L);
        verify(branchRepository, never()).saveBranch(any());
    }

    @Test
    void updateName_saveRepositoryError_shouldReturnTechnicalException() {
        RuntimeException repositoryError = new RuntimeException("Database save error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(branchRepository.existsByNameAndFranchiseId("New Name", 1L)).thenReturn(Mono.just(false));
        when(branchRepository.saveBranch(any(Branch.class))).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateName(1L, 1L, "New Name"))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.BRANCH_UPDATE_ERROR
                        && te.getCause() == repositoryError
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(branchRepository).existsByNameAndFranchiseId("New Name", 1L);
        verify(branchRepository).saveBranch(any(Branch.class));
    }

    @Test
    void updateName_businessExceptionNotWrapped_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(branchRepository.existsByNameAndFranchiseId("Duplicate", 1L)).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.updateName(1L, 1L, "Duplicate"))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && !(ex instanceof TechnicalException)
                        && be.getTechnicalMessage() == TechnicalMessage.BRANCH_NAME_ALREADY_EXISTS
                )
                .verify();
    }
}