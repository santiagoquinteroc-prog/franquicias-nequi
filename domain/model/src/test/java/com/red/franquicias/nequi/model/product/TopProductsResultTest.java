package com.red.franquicias.nequi.model.product;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TopProductsResultTest {

    @Test
    void topProductsResult_shouldCreateWithAllFields() {
        List<BranchTopProduct> results = Collections.emptyList();
        TopProductsResult tpr = new TopProductsResult(1L, "Franchise A", results);
        assertThat(tpr.franchiseId()).isEqualTo(1L);
        assertThat(tpr.franchiseName()).isEqualTo("Franchise A");
        assertThat(tpr.results()).isEmpty();
    }

    @Test
    void topProductsResult_withMultipleResults_shouldStore() {
        ProductInfo info1 = new ProductInfo(1L, "Product A", 100);
        ProductInfo info2 = new ProductInfo(2L, "Product B", 200);
        BranchTopProduct btp1 = new BranchTopProduct(1L, "Branch A", info1);
        BranchTopProduct btp2 = new BranchTopProduct(2L, "Branch B", info2);
        List<BranchTopProduct> results = List.of(btp1, btp2);
        TopProductsResult tpr = new TopProductsResult(1L, "Franchise A", results);
        assertThat(tpr.results()).hasSize(2);
        assertThat(tpr.franchiseId()).isEqualTo(1L);
    }

    @Test
    void topProductsResult_withDifferentValues_shouldStore() {
        TopProductsResult tpr1 = new TopProductsResult(1L, "A", Collections.emptyList());
        TopProductsResult tpr2 = new TopProductsResult(2L, "B", Collections.emptyList());
        assertThat(tpr1.franchiseId()).isNotEqualTo(tpr2.franchiseId());
        assertThat(tpr1.franchiseName()).isNotEqualTo(tpr2.franchiseName());
    }
}

