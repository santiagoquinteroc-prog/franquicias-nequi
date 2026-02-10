package com.red.franquicias.nequi.usecase.gettopproductsbyfranchise;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import com.red.franquicias.nequi.model.product.BranchTopProduct;
import com.red.franquicias.nequi.model.product.BranchTopProductRow;
import com.red.franquicias.nequi.model.product.gateways.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTopProductsByFranchiseUseCaseTest {

    @Mock
    private ProductRepository productRepository;

    private GetTopProductsByFranchiseUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetTopProductsByFranchiseUseCase(productRepository);
    }

    @Test
    void getTopProducts_franchiseWithBranchesAndProducts_shouldReturnTopProducts() {
        var row1 = new BranchTopProductRow(1L, "Test Franchise", 1L, "Branch 1", 10L, "Product A", 50);
        var row2 = new BranchTopProductRow(1L, "Test Franchise", 2L, "Branch 2", 20L, "Product B", 30);
        var row3 = new BranchTopProductRow(1L, "Test Franchise", 3L, "Branch 3", null, null, null);

        when(productRepository.findTopProductsByFranchiseId(1L))
                .thenReturn(Flux.just(row1, row2, row3));

        StepVerifier.create(useCase.getTopProducts(1L))
                .assertNext(result -> {
                    assertEquals(1L, result.franchiseId());
                    assertEquals("Test Franchise", result.franchiseName());

                    assertEquals(2, result.results().size());

                    assertTrue(result.results().stream().anyMatch(r ->
                            r.branchId().equals(1L)
                            && r.product() != null
                            && r.product().stock().equals(50)
                    ));

                    assertTrue(result.results().stream().anyMatch(r ->
                            r.branchId().equals(2L)
                            && r.product() != null
                            && r.product().stock().equals(30)
                    ));

                    assertFalse(result.results().stream().anyMatch(r -> r.branchId().equals(3L)));
                })
                .verifyComplete();

        verify(productRepository).findTopProductsByFranchiseId(1L);
    }

    @Test
    void getTopProducts_franchiseWithBranchesButNoProducts_shouldReturnEmptyList() {
        var row1 = new BranchTopProductRow(1L, "Test Franchise", 1L, "Branch 1", null, null, null);
        var row2 = new BranchTopProductRow(1L, "Test Franchise", 2L, "Branch 2", null, null, null);

        when(productRepository.findTopProductsByFranchiseId(1L))
                .thenReturn(Flux.just(row1, row2));

        StepVerifier.create(useCase.getTopProducts(1L))
                .assertNext(result -> {
                    assertEquals(1L, result.franchiseId());
                    assertEquals("Test Franchise", result.franchiseName());

                    assertTrue(result.results().isEmpty());
                })
                .verifyComplete();

        verify(productRepository).findTopProductsByFranchiseId(1L);
    }

    @Test
    void getTopProducts_franchiseWithSomeBranchesHavingProducts_shouldReturnOnlyBranchesWithProducts() {
        var row1 = new BranchTopProductRow(1L, "Test Franchise", 1L, "Branch 1", 10L, "Product A", 50);
        var row2 = new BranchTopProductRow(1L, "Test Franchise", 2L, "Branch 2", null, null, null);

        when(productRepository.findTopProductsByFranchiseId(1L))
                .thenReturn(Flux.just(row1, row2));

        StepVerifier.create(useCase.getTopProducts(1L))
                .assertNext(result -> {
                    assertEquals(1L, result.franchiseId());
                    assertEquals("Test Franchise", result.franchiseName());

                    assertEquals(1, result.results().size());

                    var b1 = result.results().get(0);
                    assertEquals(1L, b1.branchId());
                    assertEquals("Branch 1", b1.branchName());
                    assertNotNull(b1.product());
                    assertEquals(50, b1.product().stock());
                })
                .verifyComplete();

        verify(productRepository).findTopProductsByFranchiseId(1L);
    }

    @Test
    void getTopProducts_franchiseNotFound_shouldReturnBusinessException() {
        when(productRepository.findTopProductsByFranchiseId(999L))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.getTopProducts(999L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NOT_FOUND
                )
                .verify();

        verify(productRepository).findTopProductsByFranchiseId(999L);
    }

    @Test
    void getTopProducts_repositoryError_shouldReturnTechnicalException() {
        when(productRepository.findTopProductsByFranchiseId(1L))
                .thenReturn(Flux.error(new RuntimeException("DB down")));

        StepVerifier.create(useCase.getTopProducts(1L))
                .expectErrorMatches(ex ->
                        ex instanceof TechnicalException te
                        && te.getTechnicalMessage() == TechnicalMessage.TOP_PRODUCTS_QUERY_ERROR
                        && te.getCause() != null
                        && "DB down".equals(te.getCause().getMessage())
                )
                .verify();

        verify(productRepository).findTopProductsByFranchiseId(1L);
    }

    @Test
    void getTopProducts_multipleBranchesWithMixedProducts_shouldFilterCorrectly() {
        var row1 = new BranchTopProductRow(1L, "Test Franchise", 1L, "Branch 1", 10L, "Product A", 100);
        var row2 = new BranchTopProductRow(1L, "Test Franchise", 2L, "Branch 2", null, null, null);
        var row3 = new BranchTopProductRow(1L, "Test Franchise", 3L, "Branch 3", 30L, "Product C", 75);
        var row4 = new BranchTopProductRow(1L, "Test Franchise", 4L, "Branch 4", null, null, null);

        when(productRepository.findTopProductsByFranchiseId(1L))
                .thenReturn(Flux.just(row1, row2, row3, row4));

        StepVerifier.create(useCase.getTopProducts(1L))
                .assertNext(result -> {
                    assertEquals(1L, result.franchiseId());
                    assertEquals("Test Franchise", result.franchiseName());
                    assertEquals(2, result.results().size());

                    var branchIds = result.results().stream().map(BranchTopProduct::branchId).toList();
                    assertTrue(branchIds.contains(1L));
                    assertTrue(branchIds.contains(3L));
                    assertFalse(branchIds.contains(2L));
                    assertFalse(branchIds.contains(4L));
                })
                .verifyComplete();

        verify(productRepository).findTopProductsByFranchiseId(1L);
    }

    @Test
    void getTopProducts_businessExceptionNotWrapped_shouldReturnBusinessException() {
        when(productRepository.findTopProductsByFranchiseId(999L))
                .thenReturn(Flux.empty());

        StepVerifier.create(useCase.getTopProducts(999L))
                .expectErrorMatches(ex ->
                        ex instanceof BusinessException be
                        && !(ex instanceof TechnicalException)
                        && be.getTechnicalMessage() == TechnicalMessage.FRANCHISE_NOT_FOUND
                )
                .verify();
    }
}