package com.red.franquicias.nequi.api.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchTopProductResponseTest {

    @Test
    void branchTopProductResponse_shouldCreateSuccessfully() {
        ProductInfoResponse productInfo = new ProductInfoResponse(1L, "Product A", 100);
        BranchTopProductResponse response = new BranchTopProductResponse(10L, "Branch 1", productInfo);

        assertThat(response).isNotNull();
        assertThat(response.branchId()).isEqualTo(10L);
        assertThat(response.branchName()).isEqualTo("Branch 1");
        assertThat(response.product()).isEqualTo(productInfo);
    }

    @Test
    void branchTopProductResponse_shouldHandleDifferentProducts() {
        ProductInfoResponse productInfo = new ProductInfoResponse(2L, "Product B", 50);
        BranchTopProductResponse response = new BranchTopProductResponse(20L, "Main Branch", productInfo);

        assertThat(response).isNotNull();
        assertThat(response.branchId()).isEqualTo(20L);
        assertThat(response.branchName()).isEqualTo("Main Branch");
        assertThat(response.product().productId()).isEqualTo(2L);
        assertThat(response.product().name()).isEqualTo("Product B");
        assertThat(response.product().stock()).isEqualTo(50);
    }

    @Test
    void branchTopProductResponse_shouldHandleZeroStock() {
        ProductInfoResponse productInfo = new ProductInfoResponse(3L, "Product C", 0);
        BranchTopProductResponse response = new BranchTopProductResponse(30L, "Empty Branch", productInfo);

        assertThat(response).isNotNull();
        assertThat(response.product().stock()).isEqualTo(0);
    }
}

