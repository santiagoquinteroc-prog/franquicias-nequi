package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchRequestTest {

    @Test
    void branchRequest_shouldCreateWithName() {
        BranchRequest request = new BranchRequest("Test Branch");
        assertThat(request.name()).isEqualTo("Test Branch");
    }

    @Test
    void branchRequest_withDifferentNames_shouldStore() {
        BranchRequest request1 = new BranchRequest("Branch A");
        BranchRequest request2 = new BranchRequest("Branch B");
        assertThat(request1.name()).isEqualTo("Branch A");
        assertThat(request2.name()).isEqualTo("Branch B");
    }

    @Test
    void branchRequest_withNull_shouldHandleGracefully() {
        BranchRequest request = new BranchRequest(null);
        assertThat(request.name()).isNull();
    }

    @Test
    void branchRequest_withEmptyString_shouldStore() {
        BranchRequest request = new BranchRequest("");
        assertThat(request.name()).isEmpty();
    }

    @Test
    void branchRequest_withWhitespace_shouldStore() {
        BranchRequest request = new BranchRequest("   ");
        assertThat(request.name()).isEqualTo("   ");
    }
}

