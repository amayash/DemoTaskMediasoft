package com.mediasoft.warehouse.error.exception;

import com.mediasoft.warehouse.model.Order;

/**
 * Исключение, которое выбрасывается, когда c объектом
 * {@link Order} производят некорректный запрос, связанный с его статусом.
 */
public class IncorrectOrderStatusException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param status Статус заказа, который не подошел.
     */
    public IncorrectOrderStatusException(String status) {
        super(String.format("The order can't be changed anymore. Status: [%s]", status));
    }
}
