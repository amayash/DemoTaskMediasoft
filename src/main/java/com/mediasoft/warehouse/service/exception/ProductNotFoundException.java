package com.mediasoft.warehouse.service.exception;

import com.mediasoft.warehouse.model.Product;

import java.util.UUID;

/**
 * Исключение, которое выбрасывается, когда объект
 * {@link Product} с указанным идентификатором не был найден.
 */
public class ProductNotFoundException extends RuntimeException {

    /**
     * Конструктор для создания объекта исключения.
     *
     * @param id Идентификатор товара, который не был найден.
     */
    public ProductNotFoundException(UUID id) {
        super(String.format("Product with id or article [%s] is not found", id));
    }
}
