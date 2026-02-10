package com.red.franquicias.nequi.api.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        String path,
        int status,
        String error,
        String message
) {
}
