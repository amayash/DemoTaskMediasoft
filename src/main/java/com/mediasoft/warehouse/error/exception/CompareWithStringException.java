package com.mediasoft.warehouse.error.exception;

import com.mediasoft.warehouse.search.StringProductFilter;

/**
 * Исключение, которое выбрасывается, когда при применении фильтра
 * {@link StringProductFilter} происходит непредусмотренное сравнение.
 */
public class CompareWithStringException extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param field Поле, с которым невозможно сравнить.
     */
    public CompareWithStringException(String field) {
        super(String.format("Can't compare a string to a [%s]", field));
    }
}
