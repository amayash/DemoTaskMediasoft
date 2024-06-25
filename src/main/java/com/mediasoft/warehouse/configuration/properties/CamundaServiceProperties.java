package com.mediasoft.warehouse.configuration.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * Настройки конфигурации для сервиса камунды.
 */
@Getter
@Setter
public class CamundaServiceProperties {
    private String host;
    private CamundaServiceMethodsProperties methods;
    private MockProperties mock;

    /**
     * Настройки конфигурации для методов сервиса камунды.
     */
    @Getter
    @Setter
    public static class CamundaServiceMethodsProperties {
        private String confirmOrder;
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
