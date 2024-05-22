package com.mediasoft.warehouse.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediasoft.warehouse.dto.ViewCurrenciesDto;
import com.mediasoft.warehouse.model.enums.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Поставщик обменного курса.
 */
@Component
@RequiredArgsConstructor
public class ExchangeRateProvider {
    private final CurrencyServiceClient currencyServiceClient;

    /**
     * Получает обменный курс для указанной валюты.
     *
     * @param currency Код валюты.
     * @return Обменный курс для указанной валюты.
     */
    public BigDecimal getExchangeRate(Currency currency) {
        return Optional.ofNullable(getExchangeRateFromService(currency))
                .orElseGet(() -> getExchangeRateFromFile(currency));
    }

    /**
     * Получает обменный курс для указанной валюты из веб-сервиса.
     *
     * @param currency Код валюты.
     * @return Обменный курс для указанной валюты.
     */
    private @Nullable BigDecimal getExchangeRateFromService(Currency currency) {
        return Optional.ofNullable(currencyServiceClient.getCurrencies())
                .map(rate -> getExchangeRateByCurrency(currency, rate)).orElse(null);
    }

    /**
     * Получает обменный курс для указанной валюты из файла.
     *
     * @param currency Код валюты.
     * @return Обменный курс для указанной валюты.
     */
    private @Nullable BigDecimal getExchangeRateFromFile(Currency currency) {
        try {
            ClassPathResource resource = new ClassPathResource("exchange-rate.json");
            byte[] jsonData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode currencyNode = rootNode.get(currency.name().toLowerCase());
            if (currencyNode != null) {
                return currencyNode.decimalValue();
            } else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает обменный курс для указанной валюты.
     *
     * @param currency    Код валюты (например, "USD").
     * @param currencyDto Объект {@link ViewCurrenciesDto} с данными о курсах валют.
     * @return Обменный курс для указанной валюты.
     */
    private BigDecimal getExchangeRateByCurrency(Currency currency, ViewCurrenciesDto currencyDto) {
        return switch (currency) {
            case USD -> currencyDto.getUSD();
            case CNY -> currencyDto.getCNY();
            case EUR -> currencyDto.getEUR();
            case RUB -> BigDecimal.ONE;
        };
    }
}
