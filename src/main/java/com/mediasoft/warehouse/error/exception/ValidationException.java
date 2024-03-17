package com.mediasoft.warehouse.error.exception;

import java.util.Set;

/**
 * Исключение для ошибок валидации.
 * Содержит множество ошибок валидации, объединенных в одну строку.
 */
public class ValidationException extends RuntimeException {
    /**
     * Конструктор исключения валидации.
     *
     * @param errors Множество ошибок валидации.
     */
    public ValidationException(Set<String> errors) {
        super(String.join("\n", errors));
    }
}
