package com.red.franquicias.nequi.r2dbc.branch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchEntityTest {

    @Test
    void branchEntity_shouldCreateWithAllFields() {
        BranchEntity entity = new BranchEntity(1L, 10L, "Test Branch");
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getFranchiseId()).isEqualTo(10L);
        assertThat(entity.getName()).isEqualTo("Test Branch");
    }

    @Test
    void branchEntity_withNullId_shouldStore() {
        BranchEntity entity = new BranchEntity(null, 1L, "Test");
        assertThat(entity.getId()).isNull();
        assertThat(entity.getFranchiseId()).isEqualTo(1L);
    }

    @Test
    void branchEntity_withDifferentValues_shouldStore() {
        BranchEntity e1 = new BranchEntity(1L, 5L, "A");
        BranchEntity e2 = new BranchEntity(2L, 6L, "B");
        assertThat(e1.getId()).isNotEqualTo(e2.getId());
    }
}

