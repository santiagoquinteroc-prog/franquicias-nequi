package com.red.franquicias.nequi.api.router;

import com.red.franquicias.nequi.api.handler.HealthHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HealthRouterTest {

    @Mock
    private HealthHandler handler;

    private HealthRouter healthRouter;

    @BeforeEach
    void setUp() {
        healthRouter = new HealthRouter();
    }

    @Test
    void healthRouter_shouldBeInstantiable() {
        assertThat(healthRouter).isNotNull();
    }

    @Test
    void healthRoutes_shouldReturnRouterFunction() {
        RouterFunction<ServerResponse> result = healthRouter.healthRoutes(handler);

        assertThat(result).isNotNull();
    }
}

