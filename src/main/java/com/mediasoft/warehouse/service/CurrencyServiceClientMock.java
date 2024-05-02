package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.ViewCurrenciesDto;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Мок-реализация клиента сервиса валют для тестирования.
 */
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
