package com.red.franquicias.nequi.r2dbc.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductEntityTest {

    @Test
    void productEntity_shouldCreateWithAllFields() {
        ProductEntity entity = new ProductEntity(1L, 10L, "Test Product", 100);
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getBranchId()).isEqualTo(10L);
        assertThat(entity.getName()).isEqualTo("Test Product");
        assertThat(entity.getStock()).isEqualTo(100);
    }

    @Test
    void productEntity_withNullId_shouldStore() {
        ProductEntity entity = new ProductEntity(null, 1L, "Test", 50);
        assertThat(entity.getId()).isNull();
        assertThat(entity.getBranchId()).isEqualTo(1L);
    }

    @Test
    void productEntity_withZeroStock_shouldStore() {
        ProductEntity entity = new ProductEntity(1L, 1L, "Test", 0);
        assertThat(entity.getStock()).isEqualTo(0);
    }

    @Test
    void productEntity_withDifferentValues_shouldStore() {
        ProductEntity e1 = new ProductEntity(1L, 5L, "A", 50);
        ProductEntity e2 = new ProductEntity(2L, 6L, "B", 200);
        assertThat(e1.getId()).isNotEqualTo(e2.getId());
    }
}

