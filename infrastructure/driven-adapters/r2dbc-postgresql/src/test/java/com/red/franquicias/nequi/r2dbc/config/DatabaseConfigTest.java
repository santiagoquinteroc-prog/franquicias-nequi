package com.red.franquicias.nequi.r2dbc.config;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;

import static org.assertj.core.api.Assertions.assertThat;

class DatabaseConfigTest {

    @InjectMocks
    private DatabaseConfig databaseConfig;

    @Mock
    private ConnectionFactory connectionFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void databaseConfig_shouldBeInstantiable() {
        DatabaseConfig config = new DatabaseConfig();
        assertThat(config).isNotNull();
    }

    @Test
    void databaseConfig_classExists() {
        assertThat(DatabaseConfig.class).isNotNull();
    }

    @Test
    void initializer_shouldReturnConnectionFactoryInitializer() {
        ConnectionFactoryInitializer result = databaseConfig.initializer(connectionFactory);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(ConnectionFactoryInitializer.class);
    }
}

