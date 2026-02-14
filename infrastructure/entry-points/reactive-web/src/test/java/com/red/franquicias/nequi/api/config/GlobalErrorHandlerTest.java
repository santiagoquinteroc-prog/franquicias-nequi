package com.red.franquicias.nequi.api.config;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import com.red.franquicias.nequi.exception.BusinessException;
import com.red.franquicias.nequi.exception.TechnicalException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.test.StepVerifier;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalErrorHandlerTest {

    private GlobalErrorHandler globalErrorHandler;

    @BeforeEach
    void setUp() {
        globalErrorHandler = new GlobalErrorHandler();
    }

    @Test
    void globalErrorHandler_shouldBeInstantiable() {
        assertThat(globalErrorHandler).isNotNull();
    }

    @Test
    void handle_withConstraintViolationException_shouldReturnBadRequest() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
        );

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
        when(violation.getMessage()).thenReturn("must not be blank");

        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        StepVerifier.create(globalErrorHandler.handle(exchange, exception))
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handle_withBusinessException_shouldReturnCorrectStatus() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
        );

        BusinessException exception = new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND);

        StepVerifier.create(globalErrorHandler.handle(exchange, exception))
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isNotNull();
    }

    @Test
    void handle_withTechnicalException_shouldReturnInternalServerError() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
        );

        TechnicalException exception = new TechnicalException(new RuntimeException(), TechnicalMessage.FRANCHISE_CREATE_ERROR);

        StepVerifier.create(globalErrorHandler.handle(exchange, exception))
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isNotNull();
    }

    @Test
    void handle_withServerWebInputException_shouldReturnBadRequest() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
        );

        ServerWebInputException exception = new ServerWebInputException("Invalid input");

        StepVerifier.create(globalErrorHandler.handle(exchange, exception))
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void handle_withResponseStatusException_shouldReturnCorrectStatus() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
        );

        ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");

        StepVerifier.create(globalErrorHandler.handle(exchange, exception))
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void handle_withGenericException_shouldReturnInternalServerError() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest.get("/test")
        );

        RuntimeException exception = new RuntimeException("Unexpected error");

        StepVerifier.create(globalErrorHandler.handle(exchange, exception))
                .verifyComplete();

        assertThat(exchange.getResponse().getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

