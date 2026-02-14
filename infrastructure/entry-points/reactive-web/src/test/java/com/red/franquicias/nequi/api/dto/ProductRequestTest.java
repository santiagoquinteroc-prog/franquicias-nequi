package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRequestTest {

    @Test
    void productRequest_shouldCreateWithNameAndStock() {
        ProductRequest request = new ProductRequest("Test Product", 100);
        assertThat(request.name()).isEqualTo("Test Product");
        assertThat(request.stock()).isEqualTo(100);
    }

    @Test
    void productRequest_withDifferentValues_shouldStore() {
        ProductRequest request1 = new ProductRequest("Product A", 50);
        ProductRequest request2 = new ProductRequest("Product B", 200);
        assertThat(request1.name()).isEqualTo("Product A");
        assertThat(request1.stock()).isEqualTo(50);
        assertThat(request2.name()).isEqualTo("Product B");
        assertThat(request2.stock()).isEqualTo(200);
    }

    @Test
    void productRequest_withZeroStock_shouldStore() {
        ProductRequest request = new ProductRequest("Test", 0);
        assertThat(request.stock()).isEqualTo(0);
    }

    @Test
    void productRequest_withNegativeStock_shouldStore() {
        ProductRequest request = new ProductRequest("Test", -1);
        assertThat(request.stock()).isEqualTo(-1);
    }

    @Test
    void productRequest_withLargeStock_shouldStore() {
        ProductRequest request = new ProductRequest("Test", Integer.MAX_VALUE);
        assertThat(request.stock()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void productRequest_withNull_shouldHandleGracefully() {
        ProductRequest request = new ProductRequest(null, 100);
        assertThat(request.name()).isNull();
        assertThat(request.stock()).isEqualTo(100);
    }
}

