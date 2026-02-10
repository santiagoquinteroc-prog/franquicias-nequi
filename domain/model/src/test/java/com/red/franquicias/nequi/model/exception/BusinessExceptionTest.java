package com.red.franquicias.nequi.model.exception;


import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BusinessExceptionTest {

    @Test
    void constructor_withTechnicalMessage_shouldUseEnumMessage() {
        BusinessException exception =
                new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND);

        assertNotNull(exception);
        assertEquals(
                TechnicalMessage.BRANCH_NOT_FOUND.getMessage(),
                exception.getMessage()
        );
        assertEquals(
                TechnicalMessage.BRANCH_NOT_FOUND,
                exception.getTechnicalMessage()
        );
    }
}
