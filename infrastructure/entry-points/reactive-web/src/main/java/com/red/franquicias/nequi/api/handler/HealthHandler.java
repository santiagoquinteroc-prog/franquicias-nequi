package com.red.franquicias.nequi.api.handler;

import com.red.franquicias.nequi.logging.AdapterLogger;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class HealthHandler {
    private final AdapterLogger adapterLogger;

    public HealthHandler(AdapterLogger adapterLogger) {
        this.adapterLogger = adapterLogger;
    }

    public Mono<ServerResponse> health(ServerRequest request) {
        long startTime = adapterLogger.startTimer();
        adapterLogger.inboundStart("HealthHandler", "health", "operation=health_check");

        Map<String, String> response = Map.of("status", "ok");
        long duration = adapterLogger.calculateDuration(startTime);
        adapterLogger.inboundEnd("HealthHandler", "health", "status=ok", duration);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}


