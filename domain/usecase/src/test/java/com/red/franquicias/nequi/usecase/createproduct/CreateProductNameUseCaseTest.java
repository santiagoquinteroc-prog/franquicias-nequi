package com.red.franquicias.nequi.usecase.createproduct;

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
class CreateProductNameUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private FranchiseRepository franchiseRepository;

    private CreateProductNameUseCase useCase;

    private Franchise existingFranchise;
    private Branch existingBranch;
    private Product validProduct;

    @BeforeEach
    void setUp() {
        useCase = new CreateProductNameUseCase(productRepository, branchRepository, franchiseRepository);
        existingFranchise = new Franchise(1L, "Test Franchise");
        existingBranch = new Branch(1L, 1L, "Test Branch");
        validProduct = new Product(null, 1L, "Test Product", 10);
    }

    @Test
    void create_validProduct_shouldReturnCreatedProduct() {
        Product savedProduct = new Product(1L, 1L, "Test Product", 10);
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.existsByNameAndBranchId("Test Product", 1L)).thenReturn(Mono.just(false));
        when(productRepository.saveProduct(validProduct)).thenReturn(Mono.just(savedProduct));

        StepVerifier.create(useCase.create(validProduct, 1L))
                .expectNext(savedProduct)
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).existsByNameAndBranchId("Test Product", 1L);
        verify(productRepository).saveProduct(validProduct);
    }

    @Test
    void create_franchiseNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(999L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.create(validProduct, 999L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(999L);
        verify(branchRepository, never()).findByIdAndFranchiseId(any(), any());
        verify(productRepository, never()).existsByNameAndBranchId(any(), any());
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void create_branchNotFound_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.create(validProduct, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.BRANCH_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository, never()).existsByNameAndBranchId(any(), any());
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void create_branchNotBelongsToFranchise_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.create(validProduct, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.BRANCH_NOT_FOUND
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository, never()).existsByNameAndBranchId(any(), any());
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void create_duplicateName_shouldReturnBusinessException() {
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.existsByNameAndBranchId("Test Product", 1L)).thenReturn(Mono.just(true));

        StepVerifier.create(useCase.create(validProduct, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.PRODUCT_NAME_ALREADY_EXISTS
                )
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).existsByNameAndBranchId("Test Product", 1L);
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void create_franchiseRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.create(validProduct, 1L))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository, never()).findByIdAndFranchiseId(any(), any());
        verify(productRepository, never()).existsByNameAndBranchId(any(), any());
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void create_branchRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.create(validProduct, 1L))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository, never()).existsByNameAndBranchId(any(), any());
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void create_productExistsRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.existsByNameAndBranchId("Test Product", 1L)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.create(validProduct, 1L))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).existsByNameAndBranchId("Test Product", 1L);
        verify(productRepository, never()).saveProduct(any());
    }

    @Test
    void create_saveRepositoryError_shouldPropagateException() {
        RuntimeException repositoryError = new RuntimeException("Database save error");
        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.existsByNameAndBranchId("Test Product", 1L)).thenReturn(Mono.just(false));
        when(productRepository.saveProduct(validProduct)).thenReturn(Mono.error(repositoryError));

        StepVerifier.create(useCase.create(validProduct, 1L))
                .expectError(RuntimeException.class)
                .verify();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).existsByNameAndBranchId("Test Product", 1L);
        verify(productRepository).saveProduct(validProduct);
    }


    @Test
    void create_nullFranchiseId_shouldReturnTechnicalException() {
        StepVerifier.create(useCase.create(validProduct, null))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.INVALID_FRANCHISE_ID
                )
                .verify();

        verifyNoInteractions(franchiseRepository, branchRepository, productRepository);
    }

    @Test
    void create_nullBranchId_shouldReturnTechnicalException() {
        Product product = new Product(null, null, "Test Product", 10);

        StepVerifier.create(useCase.create(product, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.INVALID_BRANCH_ID
                )
                .verify();

        verifyNoInteractions(franchiseRepository, branchRepository, productRepository);
    }

    @Test
    void create_nullName_shouldReturnTechnicalException() {
        Product product = new Product(null, 1L, null, 10);

        StepVerifier.create(useCase.create(product, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.INVALID_PRODUCT_NAME
                )
                .verify();

        verifyNoInteractions(franchiseRepository, branchRepository, productRepository);
    }

    @Test
    void create_blankName_shouldReturnTechnicalException() {
        Product product = new Product(null, 1L, "   ", 10);

        StepVerifier.create(useCase.create(product, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.INVALID_PRODUCT_NAME
                )
                .verify();

        verifyNoInteractions(franchiseRepository, branchRepository, productRepository);
    }

    @Test
    void create_emptyName_shouldReturnTechnicalException() {
        Product product = new Product(null, 1L, "", 10);

        StepVerifier.create(useCase.create(product, 1L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.INVALID_PRODUCT_NAME
                )
                .verify();

        verifyNoInteractions(franchiseRepository, branchRepository, productRepository);
    }

    @Test
    void create_nameWithSpaces_shouldTrimAndSave() {
        Product product = new Product(null, 1L, "  Test Product  ", 10);
        Product savedProduct = new Product(1L, 1L, "Test Product", 10);

        when(franchiseRepository.findByIdFranchise(1L)).thenReturn(Mono.just(existingFranchise));
        when(branchRepository.findByIdAndFranchiseId(1L, 1L)).thenReturn(Mono.just(existingBranch));
        when(productRepository.existsByNameAndBranchId("Test Product", 1L)).thenReturn(Mono.just(false));
        when(productRepository.saveProduct(any(Product.class))).thenReturn(Mono.just(savedProduct));

        StepVerifier.create(useCase.create(product, 1L))
                .expectNext(savedProduct)
                .verifyComplete();

        verify(franchiseRepository).findByIdFranchise(1L);
        verify(branchRepository).findByIdAndFranchiseId(1L, 1L);
        verify(productRepository).existsByNameAndBranchId("Test Product", 1L);
        verify(productRepository).saveProduct(any(Product.class));
    }
}

