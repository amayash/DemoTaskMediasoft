package com.mediasoft.warehouse.service.account;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AccountServiceClientMock implements AccountServiceClient {
    /**
     * Возвращает случайные данные о счетах.
     *
     * @return Объект {@link CompletableFuture} с случайными данными о счетах.
     */
    @Override
    public CompletableFuture<Map<String, String>> getAccounts(List<String> logins) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> map = new HashMap<>();
            Random random = new Random();
            for (String login : logins) {
                String accountNumber = String.format("%08d", random.nextInt(100000000));
                map.put(login, accountNumber);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
            return map;
        });
    }
}
