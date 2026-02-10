package com.red.franquicias.nequi.model.exception;


import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.ProcessorException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ProcessorExceptionTest {

    @Test
    void constructor_withCauseAndTechnicalMessage_shouldSetFields() {
        RuntimeException cause = new RuntimeException("Root cause");

        ProcessorException exception =
                new ProcessorException(cause, TechnicalMessage.FRANCHISE_NOT_FOUND);

        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
        assertEquals(TechnicalMessage.FRANCHISE_NOT_FOUND, exception.getTechnicalMessage());
    }

    @Test
    void constructor_withMessageAndTechnicalMessage_shouldSetFields() {
        ProcessorException exception =
                new ProcessorException(
                        "Custom message",
                        TechnicalMessage.PRODUCT_NOT_FOUND
                );

        assertNotNull(exception);
        assertEquals("Custom message", exception.getMessage());
        assertEquals(TechnicalMessage.PRODUCT_NOT_FOUND, exception.getTechnicalMessage());
    }
}
