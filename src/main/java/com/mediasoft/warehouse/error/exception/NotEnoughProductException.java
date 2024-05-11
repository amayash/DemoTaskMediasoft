package com.mediasoft.warehouse.error.exception;

import com.mediasoft.warehouse.model.Product;

import java.util.UUID;

/**
 * Исключение, которое выбрасывается, когда объекта
 * {@link Product} с указанным идентификатором недостаточно.
 */
public class NotEnoughProductException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param id Идентификатор товара, которого недостаточно.
     */
    public NotEnoughProductException(UUID id, Long actual, Long expected) {
        super(String.format("Not enough product with id %1$s. Capacity: %2$s. Quantity: %3$s", id, actual, expected));
    }
}
