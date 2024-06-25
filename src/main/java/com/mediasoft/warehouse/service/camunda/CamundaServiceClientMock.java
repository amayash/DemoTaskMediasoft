package com.mediasoft.warehouse.service.camunda;

import com.mediasoft.warehouse.dto.ConfirmOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * Мок-реализация клиента сервиса камунды.
 */
@Primary
@ConditionalOnProperty(name = "app.rest.camunda-service.mock.enabled")
@Component
@Slf4j
@RequiredArgsConstructor
public class CamundaServiceClientMock implements CamundaServiceClient {
    /**
     * Возвращает сообщение принятом заказе.
     *
     * @return Объект {@link CompletableFuture} с случайными данными об ИНН.
     */
    @Override
    public String confirmOrder(ConfirmOrderDto dto) {
        return "Order status changed to CONFIRMED";
    }
}
