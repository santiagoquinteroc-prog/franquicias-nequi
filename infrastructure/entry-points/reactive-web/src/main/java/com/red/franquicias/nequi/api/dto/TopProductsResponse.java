package com.red.franquicias.nequi.api.dto;

import java.util.List;

public record TopProductsResponse(
        Long franchiseId,
        String franchiseName,
        List<BranchTopProductResponse> results
) {
}
