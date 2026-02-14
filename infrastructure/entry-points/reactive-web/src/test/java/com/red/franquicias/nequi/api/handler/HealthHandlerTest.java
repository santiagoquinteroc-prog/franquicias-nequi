package com.red.franquicias.nequi.api.handler;

import com.red.franquicias.nequi.logging.AdapterLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class HealthHandlerTest {

    @Mock
    private AdapterLogger adapterLogger;

    private HealthHandler healthHandler;

    @BeforeEach
    void setUp() {
        healthHandler = new HealthHandler(adapterLogger);
    }

    @Test
    void healthHandler_shouldBeInstantiable() {
        assertThat(healthHandler).isNotNull();
    }

    @Test
    void health_shouldReturnOkStatus() {
        MockServerRequest request = MockServerRequest.builder().build();

        Mono<ServerResponse> result = healthHandler.health(request);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertThat(response).isNotNull();
                    assertThat(response.statusCode().value()).isEqualTo(200);
                })
                .verifyComplete();
    }
}

