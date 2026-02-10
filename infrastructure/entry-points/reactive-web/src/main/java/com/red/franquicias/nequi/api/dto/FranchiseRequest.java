package com.red.franquicias.nequi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FranchiseRequest(
        @NotBlank(message = "Franchise name is required")
        @Size(max = 60, message = "Franchise name must not exceed 60 characters")
        String name
) {
}
