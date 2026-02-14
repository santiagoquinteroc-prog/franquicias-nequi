package com.red.franquicias.nequi.enums;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TechnicalMessageTest {

    @Test
    void technicalMessage_shouldHaveValidValues() {
        assertThat(TechnicalMessage.FRANCHISE_NOT_FOUND).isNotNull();
        assertThat(TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS).isNotNull();
        assertThat(TechnicalMessage.BRANCH_NOT_FOUND).isNotNull();
        assertThat(TechnicalMessage.PRODUCT_NOT_FOUND).isNotNull();
    }

    @Test
    void technicalMessage_shouldHaveMessage() {
        TechnicalMessage message = TechnicalMessage.FRANCHISE_NOT_FOUND;
        assertThat(message.getMessage()).isNotNull();
        assertThat(message.getMessage()).isNotBlank();
    }

    @Test
    void technicalMessage_shouldHaveCode() {
        TechnicalMessage message = TechnicalMessage.FRANCHISE_NOT_FOUND;
        assertThat(message.getCode()).isNotNull();
        assertThat(message.getCode()).isNotBlank();
    }
}

