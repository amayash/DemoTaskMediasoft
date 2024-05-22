package com.mediasoft.warehouse.configuration.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Настройки конфигурации для сервиса ИНН.
 */
@Getter
@Setter
public class CrmServiceProperties {
    private String host;
    private CrmServiceProperties.CrmServiceMethodsProperties methods;
    private CurrencyServiceProperties.MockProperties mock;

    /**
     * Настройки конфигурации для методов сервиса ИНН.
     */
    @Getter
    @Setter
    public static class CrmServiceMethodsProperties {
        private String getCrm;
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
