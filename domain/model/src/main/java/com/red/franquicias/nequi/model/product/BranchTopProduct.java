package com.red.franquicias.nequi.model.product;

public record BranchTopProduct(
        Long branchId,
        String branchName,
        ProductInfo product
) {
}
