package com.red.franquicias.nequi.model.franchise;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FranchiseTest {

    @Test
    void franchise_shouldCreateWithIdAndName() {
        Franchise franchise = new Franchise(1L, "Test Franchise");
        assertThat(franchise.getId()).isEqualTo(1L);
        assertThat(franchise.getName()).isEqualTo("Test Franchise");
    }

    @Test
    void franchise_withNullId_shouldStore() {
        Franchise franchise = new Franchise(null, "Test");
        assertThat(franchise.getId()).isNull();
        assertThat(franchise.getName()).isEqualTo("Test");
    }

    @Test
    void franchise_withDifferentValues_shouldStore() {
        Franchise f1 = new Franchise(1L, "A");
        Franchise f2 = new Franchise(2L, "B");
        assertThat(f1.getId()).isNotEqualTo(f2.getId());
        assertThat(f1.getName()).isNotEqualTo(f2.getName());
    }
}

