package com.red.franquicias.nequi.exception;

import com.red.franquicias.nequi.enums.TechnicalMessage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class R2dbcExceptionMapper {

    private static final String UNIQUE_VIOLATION = "23505";
    private static final String FOREIGN_KEY_VIOLATION = "23503";
    private static final String QUERY_CANCELED = "57014";
    private static final String CONNECTION_EXCEPTION_PREFIX = "08";

    public Throwable mapToBusinessOrTechnical(
            Throwable throwable,
            TechnicalMessage uniqueViolationMessage,
            TechnicalMessage foreignKeyViolationMessage,
            TechnicalMessage defaultErrorMessage
    ) {
        if (throwable instanceof BusinessException || throwable instanceof TechnicalException) {
            return throwable;
        }

        String sqlState = extractSqlState(throwable);

        if (sqlState != null) {
            if (UNIQUE_VIOLATION.equals(sqlState)) {
                return new BusinessException(uniqueViolationMessage);
            }
            if (FOREIGN_KEY_VIOLATION.equals(sqlState)) {
                return new BusinessException(foreignKeyViolationMessage);
            }
            if (QUERY_CANCELED.equals(sqlState)) {
                return new TechnicalException(throwable, defaultErrorMessage);
            }
            if (sqlState.startsWith(CONNECTION_EXCEPTION_PREFIX)) {
                return new TechnicalException(throwable, defaultErrorMessage);
            }
        }

        String exceptionClassName = throwable.getClass().getName();
        if (exceptionClassName.contains("TimeoutException") || exceptionClassName.contains("QueryTimeout")) {
            return new TechnicalException(throwable, defaultErrorMessage);
        }

        return new TechnicalException(throwable, defaultErrorMessage);
    }

    private String extractSqlState(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            String sqlState = extractSqlStateFromException(current);
            if (sqlState != null) {
                return sqlState;
            }
            String sqlStateFromMessage = extractSqlStateFromMessage(current.getMessage());
            if (sqlStateFromMessage != null) {
                return sqlStateFromMessage;
            }
            current = current.getCause();
        }
        return null;
    }

    private String extractSqlStateFromException(Throwable throwable) {
        try {
            Class<?> clazz = throwable.getClass();
            if (clazz.getName().contains("R2dbcDataIntegrityViolationException") ||
                clazz.getName().contains("R2dbcException")) {
                try {
                    java.lang.reflect.Method method = clazz.getMethod("getSqlState");
                    Object result = method.invoke(throwable);
                    if (result instanceof String) {
                        return (String) result;
                    }
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String extractSqlStateFromMessage(String message) {
        if (message == null) {
            return null;
        }
        String lowerMessage = message.toLowerCase();
        if (lowerMessage.contains("sqlstate") || lowerMessage.contains("sql state")) {
            int index = message.indexOf(":");
            if (index > 0 && index < message.length() - 5) {
                String afterColon = message.substring(index + 1).trim();
                if (afterColon.length() >= 5 && Character.isDigit(afterColon.charAt(0))) {
                    String potentialState = afterColon.substring(0, 5);
                    if (potentialState.matches("\\d{5}|\\d{2}[A-Z0-9]{3}")) {
                        return potentialState;
                    }
                }
            }
        }
        if (message.contains("23505")) {
            return "23505";
        }
        if (message.contains("23503")) {
            return "23503";
        }
        if (message.contains("57014")) {
            return "57014";
        }
        return null;
    }
}

