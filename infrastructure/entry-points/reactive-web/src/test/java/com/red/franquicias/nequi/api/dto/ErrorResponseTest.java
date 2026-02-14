package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    void errorResponse_shouldCreateSuccessfully() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse(
                timestamp,
                "/api/test",
                400,
                "Bad Request",
                "Invalid input"
        );

        assertThat(response).isNotNull();
        assertThat(response.timestamp()).isEqualTo(timestamp);
        assertThat(response.path()).isEqualTo("/api/test");
        assertThat(response.status()).isEqualTo(400);
        assertThat(response.error()).isEqualTo("Bad Request");
        assertThat(response.message()).isEqualTo("Invalid input");
    }

    @Test
    void errorResponse_shouldHandleNotFoundError() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse(
                timestamp,
                "/api/franchise/999",
                404,
                "Not Found",
                "Franchise not found"
        );

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(404);
        assertThat(response.error()).isEqualTo("Not Found");
    }

    @Test
    void errorResponse_shouldHandleServerError() {
        LocalDateTime timestamp = LocalDateTime.now();
        ErrorResponse response = new ErrorResponse(
                timestamp,
                "/api/product",
                500,
                "Internal Server Error",
                "Unexpected error occurred"
        );

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(500);
        assertThat(response.error()).isEqualTo("Internal Server Error");
    }
}

