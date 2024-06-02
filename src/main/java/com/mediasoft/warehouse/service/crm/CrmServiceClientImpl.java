package com.mediasoft.warehouse.service.crm;

import com.mediasoft.warehouse.configuration.properties.RestConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Реализация клиента сервиса ИНН.
 */
@RequiredArgsConstructor
@Service
public class CrmServiceClientImpl implements CrmServiceClient {
    private final WebClient crmServiceWebClient;
    private final RestConfigurationProperties properties;

    /**
     * Получает данные об ИНН по логинам с использованием WebClient.
     *
     * @param logins логины покупателей.
     * @return Объект {@link Map}, содержащий информацию об ИНН по логинам.
     */
    @Override
    public CompletableFuture<Map<String, String>> getCrms(List<String> logins) {
        return crmServiceWebClient
                .post()
                .uri(properties.crmServiceProperties().getHost() + properties.crmServiceProperties().getMethods().getGetCrm())
                .bodyValue(logins)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {})
                .retry(2)
                .toFuture();
    }
}
