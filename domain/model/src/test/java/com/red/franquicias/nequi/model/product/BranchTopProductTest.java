package com.red.franquicias.nequi.model.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchTopProductTest {

    @Test
    void branchTopProduct_shouldCreateWithAllFields() {
        ProductInfo productInfo = new ProductInfo(1L, "Top Product", 150);
        BranchTopProduct btp = new BranchTopProduct(1L, "Branch A", productInfo);
        assertThat(btp.branchId()).isEqualTo(1L);
        assertThat(btp.branchName()).isEqualTo("Branch A");
        assertThat(btp.product()).isEqualTo(productInfo);
    }

    @Test
    void branchTopProduct_withDifferentValues_shouldStore() {
        ProductInfo info1 = new ProductInfo(1L, "Product A", 100);
        ProductInfo info2 = new ProductInfo(2L, "Product B", 200);
        BranchTopProduct btp1 = new BranchTopProduct(1L, "Branch A", info1);
        BranchTopProduct btp2 = new BranchTopProduct(2L, "Branch B", info2);
        assertThat(btp1.branchId()).isNotEqualTo(btp2.branchId());
        assertThat(btp1.product()).isNotEqualTo(btp2.product());
    }
}

