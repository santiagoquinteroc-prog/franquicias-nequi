package com.red.franquicias.nequi.usecase.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UseCaseValidationsTest {

    @Test
    void useCaseValidations_classExists() {
        assertThat(UseCaseValidations.class).isNotNull();
    }
}


