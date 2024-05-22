package com.mediasoft.warehouse.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Интерфейс для клиента сервиса счетов.
 */
public interface AccountServiceClient {
    /**
     * Получает данные о счетах по логинам.
     *
     * @param logins логины покупателей.
     * @return Объект {@link Map}, содержащий информацию о счетах.
     */
    CompletableFuture<Map<String, String>> getAccounts(List<String> logins);
}
