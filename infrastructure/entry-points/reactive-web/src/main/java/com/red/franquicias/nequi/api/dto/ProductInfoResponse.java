package com.red.franquicias.nequi.api.dto;

public record ProductInfoResponse(
        Long productId,
        String name,
        Integer stock
) {
}
