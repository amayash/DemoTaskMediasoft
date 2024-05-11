package com.mediasoft.warehouse.filter.currency;

import com.mediasoft.warehouse.model.enums.Currency;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Провайдер, предоставляющий текущую валюту для сеанса пользователя.
 */
@SessionScope
@Component
@Data
public class CurrencyProvider {
    private Currency currency;

    public CurrencyProvider() {
        this.currency = Currency.RUB;
    }
}
