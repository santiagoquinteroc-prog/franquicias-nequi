package com.red.franquicias.nequi.api.mapper;

import com.red.franquicias.nequi.api.dto.BranchRequest;
import com.red.franquicias.nequi.api.dto.BranchResponse;
import com.red.franquicias.nequi.model.branch.Branch;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchMapperTest {

    @Test
    void toDomain_withValidRequest_shouldMapToDomain() {
        Long franchiseId = 1L;
        BranchRequest request = new BranchRequest("Test Branch");
        Branch result = BranchMapper.toDomain(request, franchiseId);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getFranchiseId()).isEqualTo(franchiseId);
        assertThat(result.getName()).isEqualTo("Test Branch");
    }

    @Test
    void toDomain_withDifferentFranchiseIds_shouldMapCorrectly() {
        Long[] franchiseIds = {1L, 100L, 999L};
        BranchRequest request = new BranchRequest("Branch");

        for (Long franchiseId : franchiseIds) {
            Branch result = BranchMapper.toDomain(request, franchiseId);
            assertThat(result.getFranchiseId()).isEqualTo(franchiseId);
        }
    }

    @Test
    void toDomain_withDifferentNames_shouldMapCorrectly() {
        String[] names = {"Branch A", "Branch B", "Test Name"};

        for (String name : names) {
            BranchRequest request = new BranchRequest(name);
            Branch result = BranchMapper.toDomain(request, 1L);
            assertThat(result.getName()).isEqualTo(name);
        }
    }

    @Test
    void toResponse_withValidBranch_shouldMapToResponse() {
        Branch branch = new Branch(1L, 10L, "Test Branch");
        BranchResponse result = BranchMapper.toResponse(branch);
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.franchiseId()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo("Test Branch");
    }

    @Test
    void toResponse_withDifferentIds_shouldMapCorrectly() {
        Long[] ids = {1L, 100L, 999L};

        for (Long id : ids) {
            Branch branch = new Branch(id, 5L, "Test");
            BranchResponse result = BranchMapper.toResponse(branch);
            assertThat(result.id()).isEqualTo(id);
        }
    }

    @Test
    void mappingRoundTrip_shouldPreserveData() {
        Long franchiseId = 42L;
        String originalName = "Round Trip Test";
        BranchRequest request = new BranchRequest(originalName);

        Branch domain = BranchMapper.toDomain(request, franchiseId);
        Branch withId = new Branch(1L, domain.getFranchiseId(), domain.getName());
        BranchResponse response = BranchMapper.toResponse(withId);

        assertThat(response.name()).isEqualTo(originalName);
        assertThat(response.franchiseId()).isEqualTo(franchiseId);
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void toResponse_withNullValues_shouldHandleCorrectly() {
        Branch branch = new Branch(null, 1L, "Test");
        BranchResponse result = BranchMapper.toResponse(branch);
        assertThat(result.id()).isNull();
        assertThat(result.franchiseId()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Test");
    }
}


