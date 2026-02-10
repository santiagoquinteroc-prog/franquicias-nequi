package com.red.franquicias.nequi.model.product;

import java.util.List;

public record TopProductsResult(
        Long franchiseId,
        String franchiseName,
        List<BranchTopProduct> results
) {
}
