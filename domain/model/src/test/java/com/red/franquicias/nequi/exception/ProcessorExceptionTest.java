package com.red.franquicias.nequi.exception;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessorExceptionTest {

    @Test
    void processorException_shouldStoreMessage() {
        ProcessorException ex = new ProcessorException("Test message", TechnicalMessage.FRANCHISE_NOT_FOUND);
        assertThat(ex.getMessage()).isEqualTo("Test message");
        assertThat(ex.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_NOT_FOUND);
    }

    @Test
    void processorException_withDifferentMessages_shouldStore() {
        ProcessorException ex1 = new ProcessorException("Message 1", TechnicalMessage.FRANCHISE_NOT_FOUND);
        ProcessorException ex2 = new ProcessorException("Message 2", TechnicalMessage.BRANCH_NOT_FOUND);
        assertThat(ex1.getMessage()).isNotEqualTo(ex2.getMessage());
    }
}

