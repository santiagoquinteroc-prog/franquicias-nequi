package com.red.franquicias.nequi.model.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void product_shouldCreateWithAllFields() {
        Product product = new Product(1L, 10L, "Test Product", 100);
        assertThat(product.getId()).isEqualTo(1L);
        assertThat(product.getBranchId()).isEqualTo(10L);
        assertThat(product.getName()).isEqualTo("Test Product");
        assertThat(product.getStock()).isEqualTo(100);
    }

    @Test
    void product_withNullId_shouldStore() {
        Product product = new Product(null, 1L, "Test", 50);
        assertThat(product.getId()).isNull();
        assertThat(product.getBranchId()).isEqualTo(1L);
    }

    @Test
    void product_withZeroStock_shouldStore() {
        Product product = new Product(1L, 1L, "Test", 0);
        assertThat(product.getStock()).isEqualTo(0);
    }

    @Test
    void product_withDifferentValues_shouldStore() {
        Product p1 = new Product(1L, 5L, "A", 50);
        Product p2 = new Product(2L, 6L, "B", 200);
        assertThat(p1.getId()).isNotEqualTo(p2.getId());
        assertThat(p1.getStock()).isNotEqualTo(p2.getStock());
    }
}

