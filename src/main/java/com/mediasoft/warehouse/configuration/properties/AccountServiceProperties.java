package com.mediasoft.warehouse.configuration.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Настройки конфигурации для сервиса счетов.
 */
@Getter
@Setter
public class AccountServiceProperties {
    private String host;
    private AccountServiceProperties.AccountServiceMethodsProperties methods;
    private CurrencyServiceProperties.MockProperties mock;

    /**
     * Настройки конфигурации для методов сервиса счетов.
     */
    @Getter
    @Setter
    public static class AccountServiceMethodsProperties {
        private String getAccount;
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
