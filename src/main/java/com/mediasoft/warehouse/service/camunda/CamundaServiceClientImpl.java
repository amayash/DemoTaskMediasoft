package com.mediasoft.warehouse.service.camunda;

import com.mediasoft.warehouse.configuration.properties.RestConfigurationProperties;
import com.mediasoft.warehouse.dto.ConfirmOrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * Реализация клиента сервиса камунды.
 */
@RequiredArgsConstructor
@Service
public class CamundaServiceClientImpl implements CamundaServiceClient {
    private final WebClient camundaServiceWebClient;
    private final RestConfigurationProperties properties;

    /**
     * Подтверждает заказ товара с использованием WebClient.
     *
     * @param dto данные для подтвекрждения заказа.
     * @return Объект {@link String
    }, содержащий полезную инфорамцию.
     */
    @Override
    public String confirmOrder(ConfirmOrderDto dto) {
        return camundaServiceWebClient
                .post()
                .uri(properties.camundaServiceProperties().getMethods().getConfirmOrder())
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<String>() {})
                .block();
    }
}
