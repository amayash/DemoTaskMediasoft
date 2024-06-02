package com.mediasoft.warehouse.service.currency;

import com.mediasoft.warehouse.dto.ViewCurrenciesDto;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Мок-реализация клиента сервиса валют для тестирования.
 */
@Primary
@ConditionalOnProperty(name = "app.rest.currency-service.mock.enabled")
@Component
public class CurrencyServiceClientMock implements CurrencyServiceClient {
    /**
     * Возвращает случайные данные о курсах валют.
     *
     * @return Объект {@link ViewCurrenciesDto} с случайными данными о курсах валют.
     */
    @Override
    public ViewCurrenciesDto getCurrencies() {
        Random random = new Random();
        return new ViewCurrenciesDto(
                BigDecimal.valueOf(random.nextDouble()),
                BigDecimal.valueOf(random.nextDouble()),
                BigDecimal.valueOf(random.nextDouble())
        );
    }
}
