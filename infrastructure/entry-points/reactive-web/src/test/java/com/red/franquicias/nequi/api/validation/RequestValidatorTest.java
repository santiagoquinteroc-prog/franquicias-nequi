package com.red.franquicias.nequi.api.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.reactive.function.server.MockServerRequest;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestValidatorTest {

    @Mock
    private Validator validator;

    private RequestValidator requestValidator;

    @BeforeEach
    void setUp() {
        requestValidator = new RequestValidator(validator);
    }

    @Test
    void requestValidator_shouldBeInstantiable() {
        assertThat(requestValidator).isNotNull();
    }

    @Test
    void validate_withNoViolations_shouldReturnBody() {
        String testBody = "test";
        when(validator.validate(testBody)).thenReturn(Collections.emptySet());

        Mono<String> result = requestValidator.validate(testBody);

        StepVerifier.create(result)
                .expectNext(testBody)
                .verifyComplete();
    }

    @Test
    void validate_withViolations_shouldReturnError() {
        String testBody = "test";
        ConstraintViolation<String> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
        when(violation.getMessage()).thenReturn("must not be blank");
        when(validator.validate(testBody)).thenReturn(Set.of(violation));

        Mono<String> result = requestValidator.validate(testBody);

        StepVerifier.create(result)
                .expectError(ServerWebInputException.class)
                .verify();
    }

    @Test
    void pathLong_withValidNumber_shouldReturnLong() {
        MockServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "123")
                .build();

        Long result = requestValidator.pathLong(request, "id");

        assertThat(result).isEqualTo(123L);
    }

    @Test
    void pathLong_withInvalidNumber_shouldThrowException() {
        MockServerRequest request = MockServerRequest.builder()
                .pathVariable("id", "invalid")
                .build();

        assertThatThrownBy(() -> requestValidator.pathLong(request, "id"))
                .isInstanceOf(ServerWebInputException.class)
                .hasMessageContaining("must be a number");
    }
}

