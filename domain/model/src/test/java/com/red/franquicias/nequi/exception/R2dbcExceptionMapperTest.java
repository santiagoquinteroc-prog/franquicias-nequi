package com.red.franquicias.nequi.exception;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

class R2dbcExceptionMapperTest {

    @Test
    void r2dbcExceptionMapper_classExists() {
        assertThat(R2dbcExceptionMapper.class).isNotNull();
    }

    @Test
    void mapToBusinessOrTechnical_withBusinessException_shouldReturnSame() {
        BusinessException exception = new BusinessException(TechnicalMessage.FRANCHISE_NOT_FOUND);

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(BusinessException.class);
        assertThat(result).isEqualTo(exception);
    }

    @Test
    void mapToBusinessOrTechnical_withTechnicalException_shouldReturnSame() {
        TechnicalException exception = new TechnicalException(new RuntimeException(), TechnicalMessage.FRANCHISE_CREATE_ERROR);

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(TechnicalException.class);
        assertThat(result).isEqualTo(exception);
    }

    @Test
    void mapToBusinessOrTechnical_withUniqueViolation23505InMessage_shouldReturnBusinessException() {
        RuntimeException exception = new RuntimeException("Error: duplicate key value violates unique constraint. SQLState: 23505");

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(BusinessException.class);
        BusinessException businessException = (BusinessException) result;
        assertThat(businessException.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS);
    }

    @Test
    void mapToBusinessOrTechnical_withForeignKeyViolation23503InMessage_shouldReturnBusinessException() {
        RuntimeException exception = new RuntimeException("Error: foreign key constraint. Message contains 23503");

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(BusinessException.class);
        BusinessException businessException = (BusinessException) result;
        assertThat(businessException.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_NOT_FOUND);
    }

    @Test
    void mapToBusinessOrTechnical_withQueryCanceled57014InMessage_shouldReturnTechnicalException() {
        RuntimeException exception = new RuntimeException("Query was canceled. Code: 57014");

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(TechnicalException.class);
        TechnicalException technicalException = (TechnicalException) result;
        assertThat(technicalException.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_CREATE_ERROR);
    }

    @Test
    void mapToBusinessOrTechnical_withTimeoutException_shouldReturnTechnicalException() {
        TimeoutException exception = new TimeoutException("Operation timed out");

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(TechnicalException.class);
        TechnicalException technicalException = (TechnicalException) result;
        assertThat(technicalException.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_CREATE_ERROR);
    }

    @Test
    void mapToBusinessOrTechnical_withGenericException_shouldReturnTechnicalException() {
        RuntimeException exception = new RuntimeException("Generic database error");

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(TechnicalException.class);
        TechnicalException technicalException = (TechnicalException) result;
        assertThat(technicalException.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_CREATE_ERROR);
    }

    @Test
    void mapToBusinessOrTechnical_withSqlStateInMessagePattern_shouldExtract() {
        RuntimeException exception = new RuntimeException("Database error sqlstate: 23505 duplicate");

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(BusinessException.class);
    }

    @Test
    void mapToBusinessOrTechnical_withNullMessage_shouldReturnTechnicalException() {
        RuntimeException exception = new RuntimeException();

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(TechnicalException.class);
    }

    @Test
    void mapToBusinessOrTechnical_withNestedCause_shouldExtractFromCause() {
        RuntimeException cause = new RuntimeException("Nested error with 23505");
        RuntimeException exception = new RuntimeException("Outer error", cause);

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(BusinessException.class);
        BusinessException businessException = (BusinessException) result;
        assertThat(businessException.getTechnicalMessage()).isEqualTo(TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS);
    }

    @Test
    void mapToBusinessOrTechnical_withConnectionException08Prefix_shouldReturnTechnicalException() {
        RuntimeException exception = new RuntimeException("Connection error. SQLState: 08001");

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(TechnicalException.class);
    }

    @Test
    void mapToBusinessOrTechnical_withQueryTimeoutInClassName_shouldReturnTechnicalException() {
        Exception exception = new Exception("Query timeout occurred") {
            @Override
            public String toString() {
                return "QueryTimeoutException: timeout";
            }
        };

        Throwable result = R2dbcExceptionMapper.mapToBusinessOrTechnical(
                exception,
                TechnicalMessage.FRANCHISE_NAME_ALREADY_EXISTS,
                TechnicalMessage.FRANCHISE_NOT_FOUND,
                TechnicalMessage.FRANCHISE_CREATE_ERROR
        );

        assertThat(result).isInstanceOf(TechnicalException.class);
    }
}


