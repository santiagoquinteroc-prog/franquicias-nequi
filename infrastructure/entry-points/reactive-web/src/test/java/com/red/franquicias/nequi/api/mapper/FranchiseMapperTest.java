package com.red.franquicias.nequi.api.mapper;

import com.red.franquicias.nequi.api.dto.FranchiseRequest;
import com.red.franquicias.nequi.api.dto.FranchiseResponse;
import com.red.franquicias.nequi.model.franchise.Franchise;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FranchiseMapperTest {

    @Test
    void toDomain_withValidRequest_shouldMapToDomain() {
        FranchiseRequest request = new FranchiseRequest("Test Franchise");
        Franchise result = FranchiseMapper.toDomain(request);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isEqualTo("Test Franchise");
    }

    @Test
    void toDomain_withDifferentNames_shouldMapCorrectly() {
        String[] names = {"Franchise A", "Franchise B", "Test Name"};

        for (String name : names) {
            FranchiseRequest request = new FranchiseRequest(name);
            Franchise result = FranchiseMapper.toDomain(request);
            assertThat(result.getName()).isEqualTo(name);
        }
    }

    @Test
    void toResponse_withValidFranchise_shouldMapToResponse() {
        Franchise franchise = new Franchise(1L, "Test Franchise");
        FranchiseResponse result = FranchiseMapper.toResponse(franchise);
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Test Franchise");
    }

    @Test
    void toResponse_withDifferentIds_shouldMapCorrectly() {
        Long[] ids = {1L, 100L, 999L};

        for (Long id : ids) {
            Franchise franchise = new Franchise(id, "Test");
            FranchiseResponse result = FranchiseMapper.toResponse(franchise);
            assertThat(result.id()).isEqualTo(id);
        }
    }

    @Test
    void mappingRoundTrip_shouldPreserveData() {
        String originalName = "Round Trip Test";
        FranchiseRequest request = new FranchiseRequest(originalName);

        Franchise domain = FranchiseMapper.toDomain(request);
        Franchise withId = new Franchise(42L, domain.getName());
        FranchiseResponse response = FranchiseMapper.toResponse(withId);

        assertThat(response.name()).isEqualTo(originalName);
        assertThat(response.id()).isEqualTo(42L);
    }

    @Test
    void toResponse_withNullValues_shouldHandleCorrectly() {
        Franchise franchise = new Franchise(null, "Test");
        FranchiseResponse result = FranchiseMapper.toResponse(franchise);
        assertThat(result.id()).isNull();
        assertThat(result.name()).isEqualTo("Test");
    }
}


