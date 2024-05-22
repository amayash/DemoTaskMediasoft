package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.configuration.properties.RestConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Реализация клиента сервиса счетов.
 */
@RequiredArgsConstructor
@Service
public class AccountServiceClientImpl implements AccountServiceClient {
    private final WebClient webClient;
    private final RestConfigurationProperties properties;

    /**
     * Получает данные о счетах по логинам с использованием WebClient.
     *
     * @param logins логины покупателей.
     * @return Объект {@link Map}, содержащий информацию о счетах по логинам.
     */
    @Override
    public CompletableFuture<Map<String, String>> getAccounts(List<String> logins) {
        return webClient
                .post()
                .uri(properties.accountServiceProperties().getHost() + properties.accountServiceProperties().getMethods().getGetAccount())
                .bodyValue(logins)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .retry(2)
                .toFuture();
    }
}
