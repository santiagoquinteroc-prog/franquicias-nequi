package com.red.franquicias.nequi.r2dbc.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseConfigTest {

    @Test
    void databaseConfig_shouldBeInstantiable() {
        DatabaseConfig config = new DatabaseConfig();
        assertThat(config).isNotNull();
    }

    @Test
    void databaseConfig_classExists() {
        assertThat(DatabaseConfig.class).isNotNull();
    }
}

