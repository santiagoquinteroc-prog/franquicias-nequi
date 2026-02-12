package com.red.franquicias.nequi.api.validation;

import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class RequestValidator {

    private final Validator validator;

    public RequestValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validate(T body) {
        var violations = validator.validate(body);
        if (violations.isEmpty()) return Mono.just(body);

        String msg = violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        return Mono.error(new ServerWebInputException(msg));
    }

    public Long pathLong(ServerRequest request, String name) {
        try {
            return Long.parseLong(request.pathVariable(name));
        } catch (Exception e) {
            throw new ServerWebInputException(name + ": must be a number");
        }
    }
}
