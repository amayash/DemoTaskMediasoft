package com.mediasoft.warehouse.error.exception;

import com.mediasoft.warehouse.model.Customer;

/**
 * Исключение, которое выбрасывается, когда объект
 * {@link Customer} с указанным идентификатором не был найден.
 */
public class CustomerNotFoundException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param id Идентификатор покупателя, который не был найден.
     */
    public CustomerNotFoundException(Long id) {
        super(String.format("Customer with id [%s] is not found", id));
    }
}
