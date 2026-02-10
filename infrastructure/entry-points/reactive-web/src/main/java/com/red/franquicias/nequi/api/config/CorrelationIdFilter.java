package com.red.franquicias.nequi.api.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class CorrelationIdFilter implements WebFilter {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    public static final String MDC_KEY = "correlationId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String cid = exchange.getRequest().getHeaders().getFirst(CORRELATION_ID_HEADER);
        if (cid == null || cid.isBlank()) {
            cid = UUID.randomUUID().toString();
        }


        exchange.getResponse().getHeaders().add(CORRELATION_ID_HEADER, cid);

        final String finalCid = cid;

        return chain.filter(exchange)
                .doFirst(() -> {
                    MDC.put(MDC_KEY, finalCid);
                    log.info("[{}] Request started {} {}", finalCid,
                            exchange.getRequest().getMethod(),
                            exchange.getRequest().getURI().getPath());
                })
                .doFinally(signalType -> {
                    log.info("[{}] Request finished ({})", finalCid, signalType);
                    MDC.remove(MDC_KEY);
                });
    }
}
