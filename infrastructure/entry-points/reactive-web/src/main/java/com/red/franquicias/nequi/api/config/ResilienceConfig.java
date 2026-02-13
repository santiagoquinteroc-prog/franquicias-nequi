package com.red.franquicias.nequi.api.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ResilienceConfig {


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
