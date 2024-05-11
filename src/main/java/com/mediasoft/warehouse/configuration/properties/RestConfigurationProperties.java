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
}
