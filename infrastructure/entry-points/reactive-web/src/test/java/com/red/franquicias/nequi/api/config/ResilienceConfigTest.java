package com.red.franquicias.nequi.api.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ResilienceConfigTest {

    @InjectMocks
    private ResilienceConfig resilienceConfig;

    @Mock
    private CircuitBreakerRegistry registry;

    @Mock
    private CircuitBreaker mockCircuitBreaker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void resilienceConfig_shouldBeInstantiable() {
        ResilienceConfig config = new ResilienceConfig();
        assertThat(config).isNotNull();
    }

    @Test
    void franchiseCircuitBreaker_shouldReturnCircuitBreaker() {
        when(registry.circuitBreaker("franchiseService")).thenReturn(mockCircuitBreaker);

        CircuitBreaker result = resilienceConfig.franchiseCircuitBreaker(registry);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockCircuitBreaker);
    }

    @Test
    void branchCircuitBreaker_shouldReturnCircuitBreaker() {
        when(registry.circuitBreaker("branchService")).thenReturn(mockCircuitBreaker);

        CircuitBreaker result = resilienceConfig.branchCircuitBreaker(registry);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockCircuitBreaker);
    }

    @Test
    void productCircuitBreaker_shouldReturnCircuitBreaker() {
        when(registry.circuitBreaker("productService")).thenReturn(mockCircuitBreaker);

        CircuitBreaker result = resilienceConfig.productCircuitBreaker(registry);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockCircuitBreaker);
    }
}

