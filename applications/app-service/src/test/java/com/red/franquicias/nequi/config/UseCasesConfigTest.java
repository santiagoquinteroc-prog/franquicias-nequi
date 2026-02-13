package com.red.franquicias.nequi.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UseCasesConfigTest {

    @Test
    void useCasesConfig_shouldBeInstantiable() {
        assertThat(UseCasesConfig.class).isNotNull();
    }
}

