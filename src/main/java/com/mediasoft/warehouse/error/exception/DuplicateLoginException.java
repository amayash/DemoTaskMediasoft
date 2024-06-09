package com.mediasoft.warehouse.error.exception;

import com.mediasoft.warehouse.model.Customer;

/**
 * Исключение, которое выбрасывается, когда объект
 * {@link Customer} с указанным логином уже существует.
 */
public class DuplicateLoginException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param login Логин покупателя, который уже существует.
     */
    public DuplicateLoginException(String login) {
        super(String.format("Customer with login [%s] already exists", login));
    }
}
