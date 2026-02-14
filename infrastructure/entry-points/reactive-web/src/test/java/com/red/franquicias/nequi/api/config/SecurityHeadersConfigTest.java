package com.red.franquicias.nequi.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SecurityHeadersConfigTest {

    private SecurityHeadersConfig securityHeadersConfig;

    @Mock
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityHeadersConfig = new SecurityHeadersConfig();
    }

    @Test
    void securityHeadersConfig_shouldBeInstantiable() {
        SecurityHeadersConfig config = new SecurityHeadersConfig();
        assertThat(config).isNotNull();
    }

    @Test
    void filter_shouldAddSecurityHeaders() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                org.springframework.mock.http.server.reactive.MockServerHttpRequest.get("/test")
        );
        when(chain.filter(any(ServerWebExchange.class))).thenReturn(Mono.empty());

        Mono<Void> result = securityHeadersConfig.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        HttpHeaders headers = exchange.getResponse().getHeaders();
        assertThat(headers.get("Content-Security-Policy")).contains("default-src 'self'; frame-ancestors 'self'; form-action 'self'");
        assertThat(headers.get("Strict-Transport-Security")).contains("max-age=31536000; includeSubDomains; preload");
        assertThat(headers.get("X-Content-Type-Options")).contains("nosniff");
        assertThat(headers.get("Server")).contains("");
        assertThat(headers.get("Cache-Control")).contains("no-store");
        assertThat(headers.get("Pragma")).contains("no-cache");
        assertThat(headers.get("Referrer-Policy")).contains("strict-origin-when-cross-origin");
    }
}

