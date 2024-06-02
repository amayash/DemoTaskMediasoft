package com.mediasoft.warehouse.service.crm;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Интерфейс для клиента сервиса ИНН.
 */
public interface CrmServiceClient {
    /**
     * Получает данные об ИНН по логинам.
     *
     * @param logins логины покупателей.
     * @return Объект {@link Map}, содержащий информацию об ИНН.
     */
    CompletableFuture<Map<String, String>> getCrms(List<String> logins);
}
