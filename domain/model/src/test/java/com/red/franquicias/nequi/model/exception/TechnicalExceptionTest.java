package com.red.franquicias.nequi.model.exception;

import static org.junit.jupiter.api.Assertions.*;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.TechnicalException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TechnicalExceptionTest {

    @Test
    void constructor_withCauseAndTechnicalMessage_shouldSetFields() {
        RuntimeException cause = new RuntimeException("DB error");

        TechnicalException exception =
                new TechnicalException(cause, TechnicalMessage.TOP_PRODUCTS_QUERY_ERROR);

        assertNotNull(exception);
        assertEquals(cause, exception.getCause());
        assertEquals(
                TechnicalMessage.TOP_PRODUCTS_QUERY_ERROR,
                exception.getTechnicalMessage()
        );
    }

    @Test
    void constructor_withTechnicalMessage_shouldUseEnumMessage() {
        TechnicalException exception =
                new TechnicalException(TechnicalMessage.TOP_PRODUCTS_QUERY_ERROR);

        assertNotNull(exception);
        assertEquals(
                TechnicalMessage.TOP_PRODUCTS_QUERY_ERROR.getMessage(),
                exception.getMessage()
        );
        assertEquals(
                TechnicalMessage.TOP_PRODUCTS_QUERY_ERROR,
                exception.getTechnicalMessage()
        );
    }
}
