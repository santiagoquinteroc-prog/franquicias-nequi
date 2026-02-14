package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FranchiseRequestTest {

    @Test
    void franchiseRequest_shouldCreateWithName() {
        FranchiseRequest request = new FranchiseRequest("Test Franchise");
        assertThat(request.name()).isEqualTo("Test Franchise");
    }

    @Test
    void franchiseRequest_withDifferentNames_shouldStore() {
        FranchiseRequest request1 = new FranchiseRequest("Franchise A");
        FranchiseRequest request2 = new FranchiseRequest("Franchise B");
        assertThat(request1.name()).isEqualTo("Franchise A");
        assertThat(request2.name()).isEqualTo("Franchise B");
        assertThat(request1.name()).isNotEqualTo(request2.name());
    }

    @Test
    void franchiseRequest_withNull_shouldHandleGracefully() {
        FranchiseRequest request = new FranchiseRequest(null);
        assertThat(request.name()).isNull();
    }

    @Test
    void franchiseRequest_withEmptyString_shouldStore() {
        FranchiseRequest request = new FranchiseRequest("");
        assertThat(request.name()).isEmpty();
    }
}

