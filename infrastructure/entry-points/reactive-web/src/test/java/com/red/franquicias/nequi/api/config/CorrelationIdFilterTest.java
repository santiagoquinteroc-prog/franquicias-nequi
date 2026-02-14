package com.red.franquicias.nequi.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CorrelationIdFilterTest {

    private CorrelationIdFilter correlationIdFilter;

    @Mock
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        correlationIdFilter = new CorrelationIdFilter();
    }

    @Test
    void correlationIdFilter_shouldBeInstantiable() {
        assertThat(correlationIdFilter).isNotNull();
    }

    @Test
    void filter_withExistingCorrelationId_shouldUseIt() {
        String existingCid = "existing-correlation-id";
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                        .header(CorrelationIdFilter.CORRELATION_ID_HEADER, existingCid)
        );
        when(chain.filter(any())).thenReturn(Mono.empty());

        StepVerifier.create(correlationIdFilter.filter(exchange, chain))
                .verifyComplete();

        HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
        assertThat(responseHeaders.getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER))
                .isEqualTo(existingCid);
    }

    @Test
    void filter_withoutCorrelationId_shouldGenerateNew() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
        );
        when(chain.filter(any())).thenReturn(Mono.empty());

        StepVerifier.create(correlationIdFilter.filter(exchange, chain))
                .verifyComplete();

        HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
        String cid = responseHeaders.getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER);
        assertThat(cid).isNotNull();
        assertThat(cid).isNotBlank();
    }

    @Test
    void filter_withBlankCorrelationId_shouldGenerateNew() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
                        .header(CorrelationIdFilter.CORRELATION_ID_HEADER, "   ")
        );
        when(chain.filter(any())).thenReturn(Mono.empty());

        StepVerifier.create(correlationIdFilter.filter(exchange, chain))
                .verifyComplete();

        HttpHeaders responseHeaders = exchange.getResponse().getHeaders();
        String cid = responseHeaders.getFirst(CorrelationIdFilter.CORRELATION_ID_HEADER);
        assertThat(cid).isNotNull();
        assertThat(cid).isNotBlank();
        assertThat(cid.trim()).isNotEmpty();
    }
}

