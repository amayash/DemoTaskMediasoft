package com.mediasoft.warehouse.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Перечисление для типа операции.
 */
public enum OperationType {
    EQUAL("="),
    GRATER_THAN_OR_EQ(">="),
    LESS_THAN_OR_EQ("<="),
    LIKE("~");

    private final String code;

    OperationType(String code) {
        this.code = code;
    }

    /**
     * Возвращает код операции.
     *
     * @return Код операции.
     */
    @JsonValue
    public String getCode() {
        return code;
    }
    
    /**
     * Получает тип операции по коду.
     *
     * @param code Код операции.
     * @return Тип операции, соответствующий переданному коду.
     */
    @JsonCreator
    public static OperationType fromCode(String code) {
        for (OperationType operationType : OperationType.values()) {
            if (operationType.name().equals(code) || operationType.code.equals(code)) {
                return operationType;
            }
        }
        return null;
    }
}