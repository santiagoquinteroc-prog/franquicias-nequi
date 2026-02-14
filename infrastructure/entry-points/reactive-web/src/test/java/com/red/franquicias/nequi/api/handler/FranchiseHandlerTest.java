package com.red.franquicias.nequi.api.handler;

import com.red.franquicias.nequi.api.dto.FranchiseRequest;
import com.red.franquicias.nequi.api.validation.RequestValidator;
import com.red.franquicias.nequi.logging.AdapterLogger;
import com.red.franquicias.nequi.model.franchise.Franchise;
import com.red.franquicias.nequi.usecase.createfranchise.CreateFranchiseUseCase;
import com.red.franquicias.nequi.usecase.updatefranchise.UpdateFranchiseNameUseCase;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FranchiseHandlerTest {

    @Mock
    private CreateFranchiseUseCase createFranchiseUseCase;

    @Mock
    private UpdateFranchiseNameUseCase updateFranchiseNameUseCase;

    private CircuitBreaker franchiseCircuitBreaker;

    @Mock
    private RequestValidator requestValidator;

    @Mock
    private AdapterLogger adapterLogger;

    private FranchiseHandler franchiseHandler;

    @BeforeEach
    void setUp() {
        CircuitBreakerConfig config = CircuitBreakerConfig.ofDefaults();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        franchiseCircuitBreaker = registry.circuitBreaker("testFranchiseCircuitBreaker");

        franchiseHandler = new FranchiseHandler(
                createFranchiseUseCase,
                updateFranchiseNameUseCase,
                franchiseCircuitBreaker,
                requestValidator,
                adapterLogger
        );
        when(adapterLogger.startTimer()).thenReturn(0L);
        when(adapterLogger.calculateDuration(anyLong())).thenReturn(100L);
    }

    @Test
    void franchiseHandler_shouldBeInstantiable() {
        assertThat(franchiseHandler).isNotNull();
    }

    @Test
    void create_shouldReturnCreatedFranchise() {
        FranchiseRequest request = new FranchiseRequest("Test Franchise");
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Test Franchise");

        MockServerRequest serverRequest = MockServerRequest.builder()
                .body(Mono.just(request));

        when(requestValidator.validate(any(FranchiseRequest.class))).thenReturn(Mono.just(request));
        when(createFranchiseUseCase.create(any(Franchise.class))).thenReturn(Mono.just(franchise));

        Mono<ServerResponse> result = franchiseHandler.create(serverRequest);

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void updateName_shouldReturnUpdatedFranchise() {
        FranchiseRequest request = new FranchiseRequest("Updated Franchise");
        Franchise franchise = new Franchise();
        franchise.setId(1L);
        franchise.setName("Updated Franchise");

        MockServerRequest serverRequest = MockServerRequest.builder()
                .pathVariable("id", "1")
                .body(Mono.just(request));

        when(requestValidator.pathLong(any(), eq("id"))).thenReturn(1L);
        when(requestValidator.validate(any(FranchiseRequest.class))).thenReturn(Mono.just(request));
        when(updateFranchiseNameUseCase.updateName(eq(1L), anyString())).thenReturn(Mono.just(franchise));

        Mono<ServerResponse> result = franchiseHandler.updateName(serverRequest);

        StepVerifier.create(result)
                .expectNextCount(1)
                .verifyComplete();
    }
}


