package com.red.franquicias.nequi.r2dbc.franchise;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FranchiseEntityTest {

    @Test
    void franchiseEntity_shouldCreateWithAllFields() {
        FranchiseEntity entity = new FranchiseEntity(1L, "Test Franchise");
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Test Franchise");
    }

    @Test
    void franchiseEntity_withNullId_shouldStore() {
        FranchiseEntity entity = new FranchiseEntity(null, "Test");
        assertThat(entity.getId()).isNull();
        assertThat(entity.getName()).isEqualTo("Test");
    }

    @Test
    void franchiseEntity_withDifferentValues_shouldStore() {
        FranchiseEntity e1 = new FranchiseEntity(1L, "A");
        FranchiseEntity e2 = new FranchiseEntity(2L, "B");
        assertThat(e1.getId()).isNotEqualTo(e2.getId());
    }
}

