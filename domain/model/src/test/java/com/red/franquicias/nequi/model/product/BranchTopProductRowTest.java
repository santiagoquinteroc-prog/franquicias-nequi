package com.red.franquicias.nequi.model.product;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BranchTopProductRowTest {

    @Test
    void branchTopProductRow_shouldCreateSuccessfully() {
        BranchTopProductRow row = new BranchTopProductRow(
                1L,
                "Franchise A",
                10L,
                "Branch 1",
                100L,
                "Product A",
                50
        );

        assertThat(row).isNotNull();
        assertThat(row.franchise_id()).isEqualTo(1L);
        assertThat(row.franchise_name()).isEqualTo("Franchise A");
        assertThat(row.branch_id()).isEqualTo(10L);
        assertThat(row.branch_name()).isEqualTo("Branch 1");
        assertThat(row.product_id()).isEqualTo(100L);
        assertThat(row.product_name()).isEqualTo("Product A");
        assertThat(row.stock()).isEqualTo(50);
    }

    @Test
    void branchTopProductRow_shouldHandleNullValues() {
        BranchTopProductRow row = new BranchTopProductRow(
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertThat(row).isNotNull();
        assertThat(row.franchise_id()).isNull();
        assertThat(row.franchise_name()).isNull();
        assertThat(row.branch_id()).isNull();
        assertThat(row.branch_name()).isNull();
        assertThat(row.product_id()).isNull();
        assertThat(row.product_name()).isNull();
        assertThat(row.stock()).isNull();
    }

    @Test
    void branchTopProductRow_shouldHandleZeroStock() {
        BranchTopProductRow row = new BranchTopProductRow(
                1L,
                "Franchise A",
                10L,
                "Branch 1",
                100L,
                "Product A",
                0
        );

        assertThat(row).isNotNull();
        assertThat(row.stock()).isEqualTo(0);
    }
}

