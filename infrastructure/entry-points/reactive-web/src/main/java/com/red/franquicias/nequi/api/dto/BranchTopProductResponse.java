package com.red.franquicias.nequi.api.dto;

public record BranchTopProductResponse(
        Long branchId,
        String branchName,
        ProductInfoResponse product
) {
}
