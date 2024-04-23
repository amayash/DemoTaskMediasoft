package com.mediasoft.warehouse.error.exception;

import com.mediasoft.warehouse.search.NumberProductFilter;

/**
 * Исключение, которое выбрасывается, когда при применении фильтра
 * {@link NumberProductFilter} происходит непредусмотренное сравнение.
 */
public class CompareWithNumberException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param field Поле, с которым невозможно сравнить.
     */
    public CompareWithNumberException(String field) {
        super(String.format("Can't compare a number to a [%s]", field));
    }
}
