package com.red.franquicias.nequi.usecase.validation;


import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import reactor.core.publisher.Mono;

public final class UseCaseValidations {

    private UseCaseValidations() {
    }

    public static Mono<Void> requireNotBlank(String value, TechnicalMessage msg) {
        if (value == null || value.trim().isEmpty()) {
            return Mono.error(new BusinessException(msg));
        }
        return Mono.empty();
    }

    public static Mono<Void> requireNotNull(Object value, TechnicalMessage msg) {
        if (value == null) {
            return Mono.error(new BusinessException(msg));
        }
        return Mono.empty();
    }
}
