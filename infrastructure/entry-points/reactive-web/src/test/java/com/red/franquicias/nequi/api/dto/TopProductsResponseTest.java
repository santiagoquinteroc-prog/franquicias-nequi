package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TopProductsResponseTest {

    @Test
    void topProductsResponse_shouldCreateSuccessfully() {
        ProductInfoResponse productInfo = new ProductInfoResponse(1L, "Product A", 100);
        BranchTopProductResponse branchProduct = new BranchTopProductResponse(10L, "Branch 1", productInfo);
        List<BranchTopProductResponse> results = Arrays.asList(branchProduct);

        TopProductsResponse response = new TopProductsResponse(5L, "Franchise 1", results);

        assertThat(response).isNotNull();
        assertThat(response.franchiseId()).isEqualTo(5L);
        assertThat(response.franchiseName()).isEqualTo("Franchise 1");
        assertThat(response.results()).hasSize(1);
        assertThat(response.results().get(0)).isEqualTo(branchProduct);
    }

    @Test
    void topProductsResponse_shouldHandleEmptyResults() {
        TopProductsResponse response = new TopProductsResponse(5L, "Franchise 1", Collections.emptyList());

        assertThat(response).isNotNull();
        assertThat(response.results()).isEmpty();
    }

    @Test
    void topProductsResponse_shouldHandleMultipleResults() {
        ProductInfoResponse product1 = new ProductInfoResponse(1L, "Product A", 100);
        ProductInfoResponse product2 = new ProductInfoResponse(2L, "Product B", 200);
        BranchTopProductResponse branch1 = new BranchTopProductResponse(10L, "Branch 1", product1);
        BranchTopProductResponse branch2 = new BranchTopProductResponse(20L, "Branch 2", product2);
        List<BranchTopProductResponse> results = Arrays.asList(branch1, branch2);

        TopProductsResponse response = new TopProductsResponse(5L, "Franchise 1", results);

        assertThat(response).isNotNull();
        assertThat(response.results()).hasSize(2);
    }
}

