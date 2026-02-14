package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductInfoResponseTest {

    @Test
    void productInfoResponse_shouldCreateSuccessfully() {
        ProductInfoResponse response = new ProductInfoResponse(1L, "Product A", 100);

        assertThat(response).isNotNull();
        assertThat(response.productId()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Product A");
        assertThat(response.stock()).isEqualTo(100);
    }

    @Test
    void productInfoResponse_shouldHandleZeroStock() {
        ProductInfoResponse response = new ProductInfoResponse(2L, "Product B", 0);

        assertThat(response).isNotNull();
        assertThat(response.stock()).isEqualTo(0);
    }

    @Test
    void productInfoResponse_shouldHandleLargeStock() {
        ProductInfoResponse response = new ProductInfoResponse(3L, "Product C", 999999);

        assertThat(response).isNotNull();
        assertThat(response.stock()).isEqualTo(999999);
    }

    @Test
    void productInfoResponse_shouldHandleLongProductName() {
        String longName = "Very Long Product Name For Testing";
        ProductInfoResponse response = new ProductInfoResponse(4L, longName, 50);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo(longName);
    }
}

