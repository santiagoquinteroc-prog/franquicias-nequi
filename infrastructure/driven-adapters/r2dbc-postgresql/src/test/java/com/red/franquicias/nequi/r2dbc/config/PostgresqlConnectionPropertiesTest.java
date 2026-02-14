package com.red.franquicias.nequi.r2dbc.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostgresqlConnectionPropertiesTest {

    @Test
    void postgresqlConnectionProperties_shouldStoreValues() {
        PostgresqlConnectionProperties props = new PostgresqlConnectionProperties(
            "localhost", 5432, "testdb", "public", "user", "password"
        );
        assertThat(props.host()).isEqualTo("localhost");
        assertThat(props.port()).isEqualTo(5432);
        assertThat(props.database()).isEqualTo("testdb");
        assertThat(props.schema()).isEqualTo("public");
        assertThat(props.username()).isEqualTo("user");
        assertThat(props.password()).isEqualTo("password");
    }

    @Test
    void postgresqlConnectionProperties_withDifferentValues_shouldStore() {
        PostgresqlConnectionProperties props1 = new PostgresqlConnectionProperties(
            "host1", 5432, "db1", "schema1", "user1", "pass1"
        );
        PostgresqlConnectionProperties props2 = new PostgresqlConnectionProperties(
            "host2", 5433, "db2", "schema2", "user2", "pass2"
        );
        assertThat(props1.host()).isNotEqualTo(props2.host());
        assertThat(props1.port()).isNotEqualTo(props2.port());
    }
}

