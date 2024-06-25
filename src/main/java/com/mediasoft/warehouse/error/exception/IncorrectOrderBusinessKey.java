package com.mediasoft.warehouse.error.exception;

public class IncorrectOrderBusinessKey extends RuntimeException {
    /**
     * Конструктор для создания объекта исключения.
     *
     * @param businessKey Бизнес-ключ заказа, который не подошел.
     */
    public IncorrectOrderBusinessKey(String businessKey) {
        super(String.format("Incorrect order business key. Business key: [%s]", businessKey));
    }
}
