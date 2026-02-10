package com.red.franquicias.nequi.usecase.updateproductstock;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductStockUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    private UpdateProductStockUseCase useCase;

    private Franchise existingFranchise;
    private Branch existingBranch;
    private Product existingProduct;

    @BeforeEach
    void setUp() {
        useCase = new UpdateProductStockUseCase(productRepository, branchRepository, franchiseRepository);
        existingFranchise = new Franchise(1L, "Test Franchise");
        existingBranch = new Branch(1L, 1L, "Test Branch");
        existingProduct = new Product(1L, 1L, "Test Product", 10);
    }

    @Test
    void updateStock_validStock_shouldReturnUpdatedProduct() {
        Product updatedProduct = new Product(1L, 1L, "Test Product", 25);
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.just(existingProduct));
        when(productRepository.saveProduct(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(useCase.updateStock(1L, 1L, 1L, 25))
                .expectNext(updatedProduct)
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository).saveProduct(any(Product.class));
    }

    @Test
    void updateStock_zeroStock_shouldReturnUpdatedProduct() {
        Product updatedProduct = new Product(1L, 1L, "Test Product", 0);
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.just(existingProduct));
        when(productRepository.saveProduct(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(useCase.updateStock(1L, 1L, 1L, 0))
                .expectNext(updatedProduct)
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository).saveProduct(any(Product.class));
    }

    @Test
    void updateStock_franchiseNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(999L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateStock(1L, 1L, 999L, 25))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(999L);
        verify(branchRepository, never()).findByIdAndFranchiseId(any(), any());
        verify(productRepository, never()).findByIdAndBranchId(any(), any());
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void updateStock_branchNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateStock(1L, 1L, 1L, 25))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.BRANCH_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository, never()).findByIdAndBranchId(any(), any());
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void updateStock_productNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(999L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateStock(999L, 1L, 1L, 25))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.PRODUCT_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(999L, 1L);
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void updateStock_productNotBelongsToBranch_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateStock(1L, 1L, 1L, 25))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.PRODUCT_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void updateStock_franchiseRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateStock(1L, 1L, 1L, 25))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository, never()).findByIdAndFranchiseId(any(), any());
        verify(productRepository, never()).findByIdAndBranchId(any(), any());
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void updateStock_branchRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateStock(1L, 1L, 1L, 25))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository, never()).findByIdAndBranchId(any(), any());
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void updateStock_productFindRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateStock(1L, 1L, 1L, 25))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void updateStock_saveRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database save error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.just(existingProduct));
        when(productRepository.saveProduct(any(Product.class))).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.updateStock(1L, 1L, 1L, 25))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository).saveProduct(any(Product.class));
    }

    @Test
    void updateStock_largeStockValue_shouldReturnUpdatedProduct() {
        Product updatedProduct = new Product(1L, 1L, "Test Product", 99999);
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.findByIdAndBranchId(1L, 1L)).thenReturn(Mono.just(existingProduct));
        when(productRepository.saveProduct(any(Product.class))).thenReturn(Mono.just(updatedProduct));

        StepVerifier.create(useCase.updateStock(1L, 1L, 1L, 99999))
                .expectNext(updatedProduct)
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).findByIdAndBranchId(1L, 1L);
        verify(productRepository).saveProduct(any(Product.class));
    }
}