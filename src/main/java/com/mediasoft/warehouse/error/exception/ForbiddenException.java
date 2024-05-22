package com.mediasoft.warehouse.error.exception;

/**
 * Исключение, обозначающее ошибку доступа (403 Forbidden).
 */
public class ForbiddenException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param message Сообщение об ошибке.
     */
    public ForbiddenException(String message) {
        super(message);
    }
}
