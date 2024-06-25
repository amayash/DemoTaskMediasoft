package com.mediasoft.warehouse.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настроек REST-сервиса.
 */
@Configuration
public class RestConfigurationProperties {
    /**
     * Создает бин для настроек сервиса валют.
     *
     * @return Настройки сервиса валют.
     */
    @Bean
    @ConfigurationProperties(prefix = "app.rest.currency-service")
    public CurrencyServiceProperties currencyServiceProperties() {
        return new CurrencyServiceProperties();
    }

    /**
     * Создает бин для настроек сервиса счетов.
     *
     * @return Настройки сервиса счетов.
     */
    @Bean
    @ConfigurationProperties(prefix = "app.rest.account-service")
    public AccountServiceProperties accountServiceProperties() {
        return new AccountServiceProperties();
    }

    /**
     * Создает бин для настроек сервиса ИНН.
     *
     * @return Настройки сервиса ИНН.
     */
    @Bean
    @ConfigurationProperties(prefix = "app.rest.crm-service")
    public CrmServiceProperties crmServiceProperties() {
        return new CrmServiceProperties();
    }

    /**
     * Создает бин для настроек сервиса камунды.
     *
     * @return Настройки сервиса камунды.
     */
    @Bean
    @ConfigurationProperties(prefix = "app.rest.camunda-service")
    public CamundaServiceProperties camundaServiceProperties() {
        return new CamundaServiceProperties();
    }
}
