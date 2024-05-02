package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.ViewCurrenciesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Реализация клиента сервиса валют.
 */
@RequiredArgsConstructor
public class CurrencyServiceClientImpl implements CurrencyServiceClient {
    private final WebClient webClient;
    @Value("${app.currency-service.methods.get-currency:/currencies}")
    private String currencyUri;

    /**
     * Получает данные о курсах валют с использованием WebClient.
     *
     * @return Объект {@link ViewCurrenciesDto}, содержащий информацию о курсах валют.
     */
    @Cacheable(value = "currencies")
    public ViewCurrenciesDto getCurrencies() {
        return webClient
                .get()
                .uri(currencyUri)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ViewCurrenciesDto>() {
                }).retry(2).block();
    }
}
