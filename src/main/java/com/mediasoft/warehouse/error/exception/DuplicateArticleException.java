package com.mediasoft.warehouse.error.exception;

import com.mediasoft.warehouse.model.Product;

/**
 * Исключение, которое выбрасывается, когда объект
 * {@link Product} с указанным артикулом уже существует.
 */
public class DuplicateArticleException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param article Артикул товара, который уже существует.
     */
    public DuplicateArticleException(String article) {
        super(String.format("Product with article [%s] already exists", article));
    }
}