package com.red.franquicias.nequi.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateProductStockRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void updateProductStockRequest_shouldCreateSuccessfully() {
        UpdateProductStockRequest request = new UpdateProductStockRequest(100);

        assertThat(request).isNotNull();
        assertThat(request.stock()).isEqualTo(100);
    }

    @Test
    void updateProductStockRequest_shouldBeValidWithZeroStock() {
        UpdateProductStockRequest request = new UpdateProductStockRequest(0);

        Set<ConstraintViolation<UpdateProductStockRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void updateProductStockRequest_shouldBeValidWithPositiveStock() {
        UpdateProductStockRequest request = new UpdateProductStockRequest(50);

        Set<ConstraintViolation<UpdateProductStockRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void updateProductStockRequest_shouldFailWhenStockIsNull() {
        UpdateProductStockRequest request = new UpdateProductStockRequest(null);

        Set<ConstraintViolation<UpdateProductStockRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("required"));
    }

    @Test
    void updateProductStockRequest_shouldFailWhenStockIsNegative() {
        UpdateProductStockRequest request = new UpdateProductStockRequest(-1);

        Set<ConstraintViolation<UpdateProductStockRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().contains("greater than or equal to 0"));
    }
}

