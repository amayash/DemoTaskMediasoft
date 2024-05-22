package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.configuration.properties.RestConfigurationProperties;
import com.mediasoft.warehouse.dto.ViewCurrenciesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Реализация клиента сервиса валют.
 */
@RequiredArgsConstructor
@Component
public class CurrencyServiceClientImpl implements CurrencyServiceClient {
    private final WebClient webClient;
    private final RestConfigurationProperties properties;

    /**
     * Получает данные о курсах валют с использованием WebClient.
     *
     * @return Объект {@link ViewCurrenciesDto}, содержащий информацию о курсах валют.
     */
    @Cacheable(value = "currencies", unless = "#result == null")
    public ViewCurrenciesDto getCurrencies() {
        return webClient
                .get()
                .uri(properties.currencyServiceProperties().getHost() + properties.currencyServiceProperties().getMethods().getGetCurrency())
                .retrieve()
                .bodyToMono(ViewCurrenciesDto.class)
                .retry(2)
                .block();
    }
}
