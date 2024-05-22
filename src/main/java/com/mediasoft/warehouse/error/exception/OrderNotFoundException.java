package com.mediasoft.warehouse.error.exception;

import com.mediasoft.warehouse.model.Order;

import java.util.UUID;

/**
 * Исключение, которое выбрасывается, когда объект
 * {@link Order} с указанным идентификатором не был найден.
 */
public class OrderNotFoundException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param id Идентификатор заказа, который не был найден.
     */
    public OrderNotFoundException(UUID id) {
        super(String.format("Order with id [%s] is not found", id));
    }
}
