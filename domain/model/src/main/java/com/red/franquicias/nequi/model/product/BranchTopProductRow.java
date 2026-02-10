package com.red.franquicias.nequi.model.product;

public record BranchTopProductRow(
        Long franchise_id,
        String franchise_name,
        Long branch_id,
        String branch_name,
        Long product_id,
        String product_name,
        Integer stock
) {}
