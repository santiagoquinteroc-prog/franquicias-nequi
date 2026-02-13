package com.red.franquicias.nequi.logging;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.UUID;

@Slf4j
public class AdapterLogger {

    private static final String CORRELATION_ID_KEY = "correlationId";

    public void inboundStart(String component, String operation, String requestSummary) {
        String correlationId = getOrGenerateCorrelationId();
        log.info(
            "event=inbound_start component={} operation={} correlationId={} request_summary={}",
            component, operation, correlationId, requestSummary
        );
    }

    public void inboundEnd(String component, String operation, String responseSummary, long durationMs) {
        String correlationId = getCorrelationId();
        log.info(
            "event=inbound_end component={} operation={} correlationId={} response_summary={} durationMs={}",
            component, operation, correlationId, responseSummary, durationMs
        );
    }

    public void outboundRequest(String target, String operation, String requestSummary) {
        String correlationId = getCorrelationId();
        log.info(
            "event=outbound_request target={} operation={} correlationId={} request_summary={}",
            target, operation, correlationId, requestSummary
        );
    }

    public void outboundResponse(String target, String operation, String responseSummary, long durationMs) {
        String correlationId = getCorrelationId();
        log.info(
            "event=outbound_response target={} operation={} correlationId={} response_summary={} durationMs={}",
            target, operation, correlationId, responseSummary, durationMs
        );
    }

    public void error(String component, String operation, Throwable exception, String extraDetails) {
        String correlationId = getCorrelationId();
        log.error(
            "event=error component={} operation={} correlationId={} exception={} message={} extra_details={}",
            component, operation, correlationId, exception.getClass().getSimpleName(),
            exception.getMessage(), extraDetails, exception
        );
    }

    public long startTimer() {
        return System.nanoTime();
    }

    public long calculateDuration(long startNano) {
        return (System.nanoTime() - startNano) / 1_000_000;
    }

    private String getOrGenerateCorrelationId() {
        String correlationId = MDC.get(CORRELATION_ID_KEY);
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
            MDC.put(CORRELATION_ID_KEY, correlationId);
        }
        return correlationId;
    }

    private String getCorrelationId() {
        String correlationId = MDC.get(CORRELATION_ID_KEY);
        return correlationId != null ? correlationId : "unknown";
    }
}




