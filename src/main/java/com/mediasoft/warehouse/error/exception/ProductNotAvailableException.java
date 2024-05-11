package com.mediasoft.warehouse.error.exception;

import com.mediasoft.warehouse.model.Product;

import java.util.UUID;

/**
 * Исключение, которое выбрасывается, когда объект
 * {@link Product} с указанным идентификатором недоступен.
 */
public class ProductNotAvailableException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param id Идентификатор товара, который недоступен.
     */
    public ProductNotAvailableException(UUID id) {
        super(String.format("Product with id [%s] not available", id));
    }
}
