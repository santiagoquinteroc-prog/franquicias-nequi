package com.red.franquicias.nequi.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CorsConfigTest {

    private CorsConfig corsConfig;

    @BeforeEach
    void setUp() {
        corsConfig = new CorsConfig();
    }

    @Test
    void corsConfig_shouldBeInstantiable() {
        CorsConfig config = new CorsConfig();
        assertThat(config).isNotNull();
    }

    @Test
    void corsWebFilter_shouldReturnCorsWebFilter() {
        List<String> allowedOrigins = Arrays.asList("http://localhost:3000", "http://localhost:4200");

        CorsWebFilter result = corsConfig.corsWebFilter(allowedOrigins);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(CorsWebFilter.class);
    }

    @Test
    void corsWebFilter_shouldHandleEmptyOrigins() {
        List<String> allowedOrigins = Arrays.asList();

        CorsWebFilter result = corsConfig.corsWebFilter(allowedOrigins);

        assertThat(result).isNotNull();
    }

    @Test
    void corsWebFilter_shouldHandleSingleOrigin() {
        List<String> allowedOrigins = Arrays.asList("http://localhost:3000");

        CorsWebFilter result = corsConfig.corsWebFilter(allowedOrigins);

        assertThat(result).isNotNull();
    }
}

