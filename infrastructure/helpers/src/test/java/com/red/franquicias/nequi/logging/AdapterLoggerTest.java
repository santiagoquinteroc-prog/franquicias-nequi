package com.red.franquicias.nequi.logging;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AdapterLoggerTest {

    private final AdapterLogger adapterLogger = new AdapterLogger();

    @Test
    void adapterLogger_startTimer_shouldReturnTimestamp() {
        long startTime = adapterLogger.startTimer();
        assertThat(startTime).isGreaterThan(0);
    }

    @Test
    void adapterLogger_calculateDuration_shouldReturnPositiveNumber() {
        long startTime = adapterLogger.startTimer();
        long duration = adapterLogger.calculateDuration(startTime);
        assertThat(duration).isGreaterThanOrEqualTo(0);
    }

    @Test
    void adapterLogger_calculateDuration_withOldTime_shouldReturnLargerDuration() {
        long oldTime = System.currentTimeMillis() - 1000;
        long duration = adapterLogger.calculateDuration(oldTime);
        assertThat(duration).isGreaterThanOrEqualTo(1000);
    }

    @Test
    void adapterLogger_inboundStart_shouldNotThrow() {
        try {
            adapterLogger.inboundStart("Handler", "method", "details");
        } catch (Exception e) {
            throw new AssertionError("inboundStart should not throw", e);
        }
    }

    @Test
    void adapterLogger_inboundEnd_shouldNotThrow() {
        try {
            adapterLogger.inboundEnd("Handler", "method", "details", 100L);
        } catch (Exception e) {
            throw new AssertionError("inboundEnd should not throw", e);
        }
    }

    @Test
    void adapterLogger_outboundRequest_shouldNotThrow() {
        try {
            adapterLogger.outboundRequest("Service", "method", "details");
        } catch (Exception e) {
            throw new AssertionError("outboundRequest should not throw", e);
        }
    }

    @Test
    void adapterLogger_outboundResponse_shouldNotThrow() {
        try {
            adapterLogger.outboundResponse("Service", "method", "details", 100);
        } catch (Exception e) {
            throw new AssertionError("outboundResponse should not throw", e);
        }
    }

    @Test
    void adapterLogger_error_shouldNotThrow() {
        try {
            RuntimeException exception = new RuntimeException("Test error");
            adapterLogger.error("Component", "operation", exception, "extra details");
        } catch (Exception e) {
            throw new AssertionError("error should not throw", e);
        }
    }
}


