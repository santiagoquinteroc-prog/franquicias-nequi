package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductResponseTest {

    @Test
    void productResponse_shouldCreateWithAllFields() {
        ProductResponse response = new ProductResponse(1L, 10L, "Test Product", 100);
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.branchId()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Test Product");
        assertThat(response.stock()).isEqualTo(100);
    }

    @Test
    void productResponse_withDifferentValues_shouldStore() {
        ProductResponse response1 = new ProductResponse(1L, 5L, "Product A", 50);
        ProductResponse response2 = new ProductResponse(2L, 6L, "Product B", 200);
        assertThat(response1.id()).isEqualTo(1L);
        assertThat(response1.stock()).isEqualTo(50);
        assertThat(response2.id()).isEqualTo(2L);
        assertThat(response2.stock()).isEqualTo(200);
    }

    @Test
    void productResponse_withZeroStock_shouldStore() {
        ProductResponse response = new ProductResponse(1L, 1L, "Test", 0);
        assertThat(response.stock()).isEqualTo(0);
    }

    @Test
    void productResponse_withNullValues_shouldHandleGracefully() {
        ProductResponse response = new ProductResponse(null, 1L, "Test", 10);
        assertThat(response.id()).isNull();
        assertThat(response.branchId()).isEqualTo(1L);
        assertThat(response.stock()).isEqualTo(10);
    }
}

