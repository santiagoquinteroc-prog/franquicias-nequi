package com.red.franquicias.nequi.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BranchRequest(
        @NotBlank(message = "Branch name is required")
        @Size(max = 60, message = "Branch name must not exceed 60 characters")
        String name
) {
}
