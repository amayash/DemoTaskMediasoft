package com.mediasoft.warehouse.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Мок-реализация клиента сервиса ИНН.
 */
@Primary
@ConditionalOnProperty(name = "app.rest.crm-service.mock.enabled")
@Component
public class CrmServiceClientMock implements CrmServiceClient {
    /**
     * Возвращает случайные данные об ИНН.
     *
     * @return Объект {@link CompletableFuture} с случайными данными об ИНН.
     */
    @Override
    public CompletableFuture<Map<String, String>> getCrms(List<String> logins) {
        CompletableFuture<Map<String, String>> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            Map<String, String> accountMap = new HashMap<>();
            Random random = new Random();
            for (String login : logins) {
                String inn = String.format("%012d", random.nextInt(1000000000));
                accountMap.put(login, inn);
            }
            future.complete(accountMap);
        });
        return future;
    }
}
