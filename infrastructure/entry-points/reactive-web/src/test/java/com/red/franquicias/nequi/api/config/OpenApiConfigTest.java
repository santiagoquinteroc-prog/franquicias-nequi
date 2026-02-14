package com.red.franquicias.nequi.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    private OpenApiConfig openApiConfig;

    @BeforeEach
    void setUp() {
        openApiConfig = new OpenApiConfig();
    }

    @Test
    void openApiConfig_shouldBeInstantiable() {
        OpenApiConfig config = new OpenApiConfig();
        assertThat(config).isNotNull();
    }

    @Test
    void customOpenAPI_shouldReturnOpenAPIConfiguration() {
        OpenAPI result = openApiConfig.customOpenAPI();

        assertThat(result).isNotNull();
        assertThat(result.getInfo()).isNotNull();
        assertThat(result.getInfo().getTitle()).isEqualTo("Franchises API");
        assertThat(result.getInfo().getVersion()).isEqualTo("1.0.0");
        assertThat(result.getInfo().getDescription()).isEqualTo("REST API for managing franchises, branches, and products");
    }

    @Test
    void publicApi_shouldReturnGroupedOpenApi() {
        GroupedOpenApi result = openApiConfig.publicApi();

        assertThat(result).isNotNull();
        assertThat(result.getGroup()).isEqualTo("franquicias-api");
    }
}


