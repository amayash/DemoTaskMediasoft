package com.mediasoft.warehouse.configuration.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Настройки конфигурации для сервиса валют.
 */
@Getter
@Setter
public class CurrencyServiceProperties {
    private String host;
    private CurrencyServiceMethodsProperties methods;
    private MockProperties mock;

    /**
     * Настройки конфигурации для методов сервиса валют.
     */
    @Getter
    @Setter
    public static class CurrencyServiceMethodsProperties {
        private String getCurrency;
    }

    /**
     * Настройки конфигурации для мокирования.
     */
    @Getter
    @Setter
    public static class MockProperties {
        private boolean enabled;
    }
}