package com.red.franquicias.nequi.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class R2dbcExceptionMapperTest {

    @Test
    void r2dbcExceptionMapper_classExists() {
        assertThat(R2dbcExceptionMapper.class).isNotNull();
    }
}

