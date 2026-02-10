package com.red.franquicias.nequi.api.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class ResilienceConfig {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofSeconds(10))
                .permittedNumberOfCallsInHalfOpenState(3)
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .minimumNumberOfCalls(5)
                .build();

        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public CircuitBreaker franchiseCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("franchiseService");
    }

    @Bean
    public CircuitBreaker branchCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("branchService");
    }

    @Bean
    public CircuitBreaker productCircuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("productService");
    }
}
