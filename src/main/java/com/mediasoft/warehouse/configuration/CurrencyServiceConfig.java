package com.mediasoft.warehouse.configuration;

import com.mediasoft.warehouse.service.CurrencyServiceClient;
import com.mediasoft.warehouse.service.CurrencyServiceClientImpl;
import com.mediasoft.warehouse.service.CurrencyServiceClientMock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Конфигурационный класс для настройки клиента сервиса валют {@link CurrencyServiceClient}.
 */
@Configuration
@RequiredArgsConstructor
public class CurrencyServiceConfig {
    @Value("${app.currency-service.stub-enable:false}")
    private boolean stubEnable;
    private final WebClient webClient;

    /**
     * Создает экземпляр клиента сервиса валют в зависимости от конфигурационного файла.
     *
     * @return Реализация {@link CurrencyServiceClient}, представляющий клиент сервиса валют.
     */
    @Bean
    public CurrencyServiceClient currencyServiceClient() {
        if (stubEnable) {
            return new CurrencyServiceClientMock();
        } else {
            return new CurrencyServiceClientImpl(webClient);
        }
    }
}
