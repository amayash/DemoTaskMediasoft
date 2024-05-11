package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.ViewCurrenciesDto;

/**
 * Интерфейс для клиента сервиса валют.
 */
public interface CurrencyServiceClient {
    /**
     * Получает данные о курсах валют.
     *
     * @return Объект {@link ViewCurrenciesDto}, содержащий информацию о курсах валют.
     */
    ViewCurrenciesDto getCurrencies();
}
