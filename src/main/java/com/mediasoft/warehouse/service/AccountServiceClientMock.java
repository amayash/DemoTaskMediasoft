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
 * Мок-реализация клиента сервиса счетов.
 */
@Primary
@ConditionalOnProperty(name = "app.rest.account-service.mock.enabled")
@Component
public class AccountServiceClientMock implements AccountServiceClient {
    /**
     * Возвращает случайные данные о счетах.
     *
     * @return Объект {@link CompletableFuture} с случайными данными о счетах.
     */
    @Override
    public CompletableFuture<Map<String, String>> getAccounts(List<String> logins) {
        CompletableFuture<Map<String, String>> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            Map<String, String> accountMap = new HashMap<>();
            Random random = new Random();
            for (String login : logins) {
                String accountNumber = String.format("%08d", random.nextInt(100000000));
                accountMap.put(login, accountNumber);
            }
            future.complete(accountMap);
        });
        return future;
    }
}
