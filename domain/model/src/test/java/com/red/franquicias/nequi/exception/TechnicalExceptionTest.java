package com.red.franquicias.nequi.exception;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TechnicalExceptionTest {

    @Test
    void technicalException_withCauseAndMessage() {
        RuntimeException cause = new RuntimeException("DB error");
        TechnicalException ex = new TechnicalException(cause, TechnicalMessage.FRANCHISE_CREATE_ERROR);
        assertThat(ex.getCause()).isEqualTo(cause);
        assertThat(ex.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_CREATE_ERROR);
    }

    @Test
    void technicalException_withOnlyMessage() {
        TechnicalException ex = new TechnicalException(TechnicalMessage.FRANCHISE_CREATE_ERROR);
        assertThat(ex.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_CREATE_ERROR);
    }

    @Test
    void technicalException_shouldInheritFromProcessorException() {
        TechnicalException ex = new TechnicalException(TechnicalMessage.FRANCHISE_CREATE_ERROR);
        assertThat(ex).isInstanceOf(ProcessorException.class);
    }

    @Test
    void technicalException_withDifferentCauses_shouldStore() {
        RuntimeException cause1 = new RuntimeException("Error 1");
        RuntimeException cause2 = new RuntimeException("Error 2");
        TechnicalException ex1 = new TechnicalException(cause1, TechnicalMessage.FRANCHISE_CREATE_ERROR);
        TechnicalException ex2 = new TechnicalException(cause2, TechnicalMessage.FRANCHISE_CREATE_ERROR);
        assertThat(ex1.getCause()).isNotEqualTo(ex2.getCause());
    }
}

