package com.mediasoft.warehouse.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;

/**
 * Перечисление для валюты цены товаров.
 */
public enum Currency {
    USD("usd"),
    EUR("eur"),
    CNY("cny"),
    RUB("rub");

    private final String name;

    Currency(String name) {
        this.name = name;
    }

    /**
     * Возвращает название валюты.
     *
     * @return Название валюты.
     */
    @JsonValue
    public String getName() {
        return name.toUpperCase();
    }

    /**
     * Получает валюту по названию.
     *
     * @param name Название валюты.
     * @return Тип валюты, соответствующий переданному названию.
     */
    @JsonCreator
    public static @Nullable Currency fromValue(String name) {
        for (Currency currency : Currency.values()) {
            if (currency.name().equals(name) || currency.name.equals(name)) {
                return currency;
            }
        }
        return null;
    }
}
