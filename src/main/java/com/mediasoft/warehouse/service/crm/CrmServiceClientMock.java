package com.mediasoft.warehouse.service.crm;

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
 * Мок-реализация клиента сервиса ИНН.
 */
@Primary
@ConditionalOnProperty(name = "app.rest.crm-service.mock.enabled")
@Component
@Slf4j
public class CrmServiceClientMock implements CrmServiceClient {
    /**
     * Возвращает случайные данные об ИНН.
     *
     * @return Объект {@link CompletableFuture} с случайными данными об ИНН.
     */
    @Override
    public CompletableFuture<Map<String, String>> getCrms(List<String> logins) {
        return CompletableFuture.supplyAsync(() -> {
            Map<String, String> map = new HashMap<>();
            Random random = new Random();
            for (String login : logins) {
                String accountNumber = String.format("%012d", random.nextInt(1000000000));
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
