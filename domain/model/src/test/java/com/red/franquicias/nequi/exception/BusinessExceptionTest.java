package com.red.franquicias.nequi.exception;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessExceptionTest {

    @Test
    void businessException_shouldCreateWithTechnicalMessage() {
        BusinessException ex = new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND);
        assertThat(ex.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_NOT_FOUND);
    }

    @Test
    void businessException_shouldInheritFromProcessorException() {
        BusinessException ex = new BusinessException(TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS);
        assertThat(ex).isInstanceOf(ProcessorException.class);
    }

    @Test
    void businessException_withDifferentMessages_shouldStore() {
        BusinessException ex1 = new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND);
        BusinessException ex2 = new BusinessException(TechnicalMessage.BRANCH_NOT_FOUND);
        assertThat(ex1.getTechnicalMessage()).isNotEqualTo(ex2.getTechnicalMessage());
    }
}

