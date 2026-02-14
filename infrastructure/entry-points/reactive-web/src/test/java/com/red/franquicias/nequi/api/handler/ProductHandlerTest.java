package com.red.franquicias.nequi.api.handler;

import com.red.franquicias.nequi.api.dto.ProductRequest;
import com.red.franquicias.nequi.api.dto.UpdateProductNameRequest;
import com.red.franquicias.nequi.api.dto.UpdateProductStockRequest;
import com.red.franquicias.nequi.api.validation.RequestValidator;
import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.product.Product;
import com.red.franquicias.nequi.model.product.TopProductsResult;
import com.red.franquicias.nequi.usecase.createproduct.CreateProductNameUseCase;
import com.red.franquicias.nequi.usecase.gettopproductsbyfranchise.GetTopProductsByFranchiseUseCase;
import com.red.franquicias.nequi.usecase.removeproduct.RemoveProductUseCase;
import com.red.franquicias.nequi.usecase.updateproduct.UpdateProductNameUseCase;
import com.red.franquicias.nequi.usecase.updateproductstock.UpdateProductStockUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductHandlerTest {

    @Mock
    private CreateProductNameUseCase createProductUseCase;

    @Mock
    private UpdateProductNameUseCase updateProductNameUseCase;

    @Mock
    private UpdateProductStockUseCase updateProductStockUseCase;

    @Mock
    private RemoveProductUseCase removeProductUseCase;

    @Mock
    private GetTopProductsByFranchiseUseCase getTopProductsByFranchiseUseCase;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private AdapterLogger adapterLogger;

    private ProductHandler productHandler;

    @BeforeEach
    void setUp() {
        productHandler = new ProductHandler(
                createProductUseCase,
                updateProductNameUseCase,
                updateProductStockUseCase,
                removeProductUseCase,
                getTopProductsByFranchiseUseCase,
                requestValidator,
                adapterLogger
        );
        when(adapterLogger.startTimer()).thenReturn(0L);
        when(adapterLogger.calculateDuration(anyLong())).thenReturn(100L);
    }

    @Test
    void productHandler_shouldBeInstantiable() {
        assertThat(productHandler).isNotNull();
    }

    @Test
    void create_shouldReturnCreatedProduct() {
        ProductRequest request = new ProductRequest("Test Product", 100);
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setStock(100);
        product.setBranchId(20L);

        MockServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("franchiseId", "10")
                .pathVariable("branchId", "20")
                .body(Mono.just(request));

        when(requestValidator.pathLong(any(), eq("franchiseId"))).thenReturn(10L);
        when(requestValidator.pathLong(any(), eq("branchId"))).thenReturn(20L);
        when(requestValidator.validate(any(ProductRequest.class))).thenReturn(Mono.just(request));
        when(createProductUseCase.create(any(Product.class), eq(10L))).thenReturn(Mono.just(product));

        Mono<ServerResponse> result = productHandler.create(serverRequest);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.statusCode().value()).isEqualTo(201);
                })
                .verifyComplete();
    }

    @Test
    void updateName_shouldReturnUpdatedProduct() {
        UpdateProductNameRequest request = new UpdateProductNameRequest("Updated Product");
        Product product = new Product();
        product.setId(1L);
        product.setName("Updated Product");
        product.setBranchId(20L);

        MockServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("franchiseId", "10")
                .pathVariable("branchId", "20")
                .pathVariable("productId", "1")
                .body(Mono.just(request));

        when(requestValidator.pathLong(any(), eq("franchiseId"))).thenReturn(10L);
        when(requestValidator.pathLong(any(), eq("branchId"))).thenReturn(20L);
        when(requestValidator.pathLong(any(), eq("productId"))).thenReturn(1L);
        when(requestValidator.validate(any(UpdateProductNameRequest.class))).thenReturn(Mono.just(request));
        when(updateProductNameUseCase.updateName(eq(1L), eq(20L), eq(10L), anyString())).thenReturn(Mono.just(product));

        Mono<ServerResponse> result = productHandler.updateName(serverRequest);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.statusCode().value()).isEqualTo(200);
                })
                .verifyComplete();
    }

    @Test
    void updateStock_shouldReturnUpdatedProduct() {
        UpdateProductStockRequest request = new UpdateProductStockRequest(150);
        Product product = new Product();
        product.setId(1L);
        product.setStock(150);
        product.setBranchId(20L);

        MockServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("franchiseId", "10")
                .pathVariable("branchId", "20")
                .pathVariable("productId", "1")
                .body(Mono.just(request));

        when(requestValidator.pathLong(any(), eq("franchiseId"))).thenReturn(10L);
        when(requestValidator.pathLong(any(), eq("branchId"))).thenReturn(20L);
        when(requestValidator.pathLong(any(), eq("productId"))).thenReturn(1L);
        when(requestValidator.validate(any(UpdateProductStockRequest.class))).thenReturn(Mono.just(request));
        when(updateProductStockUseCase.updateStock(eq(1L), eq(20L), eq(10L), eq(150))).thenReturn(Mono.just(product));

        Mono<ServerResponse> result = productHandler.updateStock(serverRequest);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.statusCode().value()).isEqualTo(200);
                })
                .verifyComplete();
    }

    @Test
    void remove_shouldReturnNoContent() {
        MockServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("franchiseId", "10")
                .pathVariable("branchId", "20")
                .pathVariable("productId", "1")
                .build();

        when(requestValidator.pathLong(any(), eq("franchiseId"))).thenReturn(10L);
        when(requestValidator.pathLong(any(), eq("branchId"))).thenReturn(20L);
        when(requestValidator.pathLong(any(), eq("productId"))).thenReturn(1L);
        when(removeProductUseCase.remove(eq(1L), eq(20L), eq(10L))).thenReturn(Mono.empty());

        Mono<ServerResponse> result = productHandler.remove(serverRequest);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.statusCode().value()).isEqualTo(204);
                })
                .verifyComplete();
    }

    @Test
    void getTopProducts_shouldReturnTopProductsList() {
        TopProductsResult topProductsResult = new TopProductsResult(1L, "Test Franchise", Collections.emptyList());

        MockServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("franchiseId", "1")
                .build();

        when(requestValidator.pathLong(any(), eq("franchiseId"))).thenReturn(1L);
        when(getTopProductsByFranchiseUseCase.getTopProducts(eq(1L))).thenReturn(Mono.just(topProductsResult));

        Mono<ServerResponse> result = productHandler.getTopProducts(serverRequest);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.statusCode().value()).isEqualTo(200);
                })
                .verifyComplete();
    }
}

