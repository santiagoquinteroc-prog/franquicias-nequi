package com.red.franquicias.nequi.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateProductNameRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void updateProductNameRequest_shouldCreateSuccessfully() {
        UpdateProductNameRequest request = new UpdateProductNameRequest("Product Name");

        assertThat(request).isNotNull();
        assertThat(request.name()).isEqualTo("Product Name");
    }

    @Test
    void updateProductNameRequest_shouldBeValid() {
        UpdateProductNameRequest request = new UpdateProductNameRequest("Valid Product");

        Set<ConstraintViolation<UpdateProductNameRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void updateProductNameRequest_shouldFailWhenNameIsNull() {
        UpdateProductNameRequest request = new UpdateProductNameRequest(null);

        Set<ConstraintViolation<UpdateProductNameRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("required"));
    }

    @Test
    void updateProductNameRequest_shouldFailWhenNameIsBlank() {
        UpdateProductNameRequest request = new UpdateProductNameRequest("   ");

        Set<ConstraintViolation<UpdateProductNameRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("required"));
    }

    @Test
    void updateProductNameRequest_shouldFailWhenNameExceedsMaxLength() {
        String longName = "a".repeat(61);
        UpdateProductNameRequest request = new UpdateProductNameRequest(longName);

        Set<ConstraintViolation<UpdateProductNameRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("60 characters"));
    }
}

