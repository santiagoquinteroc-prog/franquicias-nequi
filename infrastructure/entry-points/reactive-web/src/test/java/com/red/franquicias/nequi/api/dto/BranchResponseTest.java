package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchResponseTest {

    @Test
    void branchResponse_shouldCreateWithIdFranchiseIdAndName() {
        BranchResponse response = new BranchResponse(1L, 10L, "Test Branch");
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.franchiseId()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Test Branch");
    }

    @Test
    void branchResponse_withDifferentValues_shouldStore() {
        BranchResponse response1 = new BranchResponse(1L, 5L, "Branch A");
        BranchResponse response2 = new BranchResponse(2L, 6L, "Branch B");
        assertThat(response1.id()).isEqualTo(1L);
        assertThat(response1.franchiseId()).isEqualTo(5L);
        assertThat(response2.id()).isEqualTo(2L);
        assertThat(response2.franchiseId()).isEqualTo(6L);
    }

    @Test
    void branchResponse_withNullValues_shouldHandleGracefully() {
        BranchResponse response = new BranchResponse(null, 1L, "Test");
        assertThat(response.id()).isNull();
        assertThat(response.franchiseId()).isEqualTo(1L);
    }
}

