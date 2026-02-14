package com.red.franquicias.nequi.model.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductInfoTest {

    @Test
    void productInfo_shouldCreateWithAllFields() {
        ProductInfo info = new ProductInfo(1L, "Test Product", 100);
        assertThat(info.productId()).isEqualTo(1L);
        assertThat(info.name()).isEqualTo("Test Product");
        assertThat(info.stock()).isEqualTo(100);
    }

    @Test
    void productInfo_withDifferentValues_shouldStore() {
        ProductInfo info1 = new ProductInfo(1L, "A", 50);
        ProductInfo info2 = new ProductInfo(2L, "B", 200);
        assertThat(info1.productId()).isNotEqualTo(info2.productId());
        assertThat(info1.stock()).isNotEqualTo(info2.stock());
    }

    @Test
    void productInfo_withZeroStock_shouldStore() {
        ProductInfo info = new ProductInfo(1L, "Test", 0);
        assertThat(info.stock()).isEqualTo(0);
    }
}

