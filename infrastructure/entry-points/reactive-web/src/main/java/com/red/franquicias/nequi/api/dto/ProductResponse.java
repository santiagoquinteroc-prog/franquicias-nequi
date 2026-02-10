package com.red.franquicias.nequi.api.dto;

public record ProductResponse(
        Long id,
        Long branchId,
        String name,
        Integer stock
) {
}
