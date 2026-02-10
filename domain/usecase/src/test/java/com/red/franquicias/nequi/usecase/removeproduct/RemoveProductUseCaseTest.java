package com.red.franquicias.nequi.usecase.removeproduct;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.model.branch.Branch;
import com.red.franquicias.nequi.model.branch.gateways.BranchRepository;
import com.red.franquicias.nequi.model.franchise.Franchise;
import com.red.franquicias.nequi.model.franchise.gateways.FranchiseRepository;
import com.red.franquicias.nequi.model.product.Product;
import com.red.franquicias.nequi.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveProductUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    private RemoveProductUseCase useCase;

    private Franchise existingFranchise;
    private Branch existingBranch;
    private Product existingProduct;

    @BeforeEach
    void setUp() {
        useCase = new RemoveProductUseCase(productRepository, branchRepository, franchiseRepository);
        existingFranchise = new Franchise(1L, "Test Franchise");
        existingBranch = new Branch(1L, 1L, "Test Branch");
        existingProduct = new Product(1L, 1L, "Test Product", 10);
    }

    @Test
    void remove_validProduct_shouldCompleteSuccessfully() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.just(existingProduct));
        when(productRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.remove(1L, 1L, 1L))
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository).deleteById(1L);
    }

    @Test
    void remove_franchiseNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(999L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.remove(1L, 1L, 999L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(999L);
        verify(branchRepository, never()).findByIdAndFranchiseId(any(), any());
        verify(productRepository, never()).findByIdAndBranchId(any(), any());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void remove_branchNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.remove(1L, 1L, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.BRANCH_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository, never()).findByIdAndBranchId(any(), any());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void remove_branchNotBelongsToFranchise_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.remove(1L, 1L, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.BRANCH_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository, never()).findByIdAndBranchId(any(), any());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void remove_productNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(999L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.remove(999L, 1L, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.PRODUCT_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(999L, 1L);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void remove_productNotBelongsToBranch_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.remove(1L, 1L, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.PRODUCT_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void remove_franchiseRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.remove(1L, 1L, 1L))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository, never()).findByIdAndFranchiseId(any(), any());
        verify(productRepository, never()).findByIdAndBranchId(any(), any());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void remove_branchRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.remove(1L, 1L, 1L))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository, never()).findByIdAndBranchId(any(), any());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void remove_productRepositoryFindError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.remove(1L, 1L, 1L))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    void remove_productRepositoryDeleteError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database delete error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.just(existingProduct));
        when(productRepository.deleteById(1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.remove(1L, 1L, 1L))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository).deleteById(1L);
    }
}