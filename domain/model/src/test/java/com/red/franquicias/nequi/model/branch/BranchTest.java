package com.red.franquicias.nequi.model.branch;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchTest {

    @Test
    void branch_shouldCreateWithAllFields() {
        Branch branch = new Branch(1L, 10L, "Test Branch");
        assertThat(branch.getId()).isEqualTo(1L);
        assertThat(branch.getFranchiseId()).isEqualTo(10L);
        assertThat(branch.getName()).isEqualTo("Test Branch");
    }

    @Test
    void branch_withNullId_shouldStore() {
        Branch branch = new Branch(null, 1L, "Test");
        assertThat(branch.getId()).isNull();
        assertThat(branch.getFranchiseId()).isEqualTo(1L);
    }

    @Test
    void branch_withDifferentValues_shouldStore() {
        Branch b1 = new Branch(1L, 5L, "A");
        Branch b2 = new Branch(2L, 6L, "B");
        assertThat(b1.getId()).isNotEqualTo(b2.getId());
        assertThat(b1.getFranchiseId()).isNotEqualTo(b2.getFranchiseId());
    }
}

