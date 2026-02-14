package com.red.franquicias.nequi.api.router;

import com.red.franquicias.nequi.api.handler.ProductHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProductRouterTest {

    @Mock
    private ProductHandler handler;

    private ProductRouter productRouter;

    @BeforeEach
    void setUp() {
        productRouter = new ProductRouter();
    }

    @Test
    void productRouter_shouldBeInstantiable() {
        assertThat(productRouter).isNotNull();
    }

    @Test
    void productRoutes_shouldReturnRouterFunction() {
        RouterFunction<ServerResponse> result = productRouter.productRoutes(handler);

        assertThat(result).isNotNull();
    }
}

