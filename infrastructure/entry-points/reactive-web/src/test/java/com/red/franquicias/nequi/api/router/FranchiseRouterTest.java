package com.red.franquicias.nequi.api.router;

import com.red.franquicias.nequi.api.handler.FranchiseHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FranchiseRouterTest {

    @Mock
    private FranchiseHandler handler;

    private FranchiseRouter franchiseRouter;

    @BeforeEach
    void setUp() {
        franchiseRouter = new FranchiseRouter();
    }

    @Test
    void franchiseRouter_shouldBeInstantiable() {
        assertThat(franchiseRouter).isNotNull();
    }

    @Test
    void franchiseRoutes_shouldReturnRouterFunction() {
        RouterFunction<ServerResponse> result = franchiseRouter.franchiseRoutes(handler);

        assertThat(result).isNotNull();
    }
}

