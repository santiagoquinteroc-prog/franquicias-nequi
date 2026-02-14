package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FranchiseResponseTest {

    @Test
    void franchiseResponse_shouldCreateWithIdAndName() {
        FranchiseResponse response = new FranchiseResponse(1L, "Test Franchise");
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Test Franchise");
    }

    @Test
    void franchiseResponse_withDifferentValues_shouldStore() {
        FranchiseResponse response1 = new FranchiseResponse(1L, "Franchise A");
        FranchiseResponse response2 = new FranchiseResponse(2L, "Franchise B");
        assertThat(response1.id()).isEqualTo(1L);
        assertThat(response2.id()).isEqualTo(2L);
        assertThat(response1.name()).isNotEqualTo(response2.name());
    }

    @Test
    void franchiseResponse_withNullId_shouldStore() {
        FranchiseResponse response = new FranchiseResponse(null, "Test");
        assertThat(response.id()).isNull();
    }
}

