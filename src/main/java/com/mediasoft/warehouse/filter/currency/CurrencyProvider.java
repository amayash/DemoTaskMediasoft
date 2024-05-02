package com.mediasoft.warehouse.filter.currency;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * Провайдер, предоставляющий текущую валюту для сеанса пользователя.
 */
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
@RequiredArgsConstructor
public class CurrencyProvider {
    public static final String CURRENCY_HEADER = "currency";
    public static final String DEFAULT_CURRENCY = "RUB";
    private final HttpSession session;

    /**
     * Возвращает текущую валюту из сеанса пользователя.
     *
     * @return Код текущей валюты или null, если валюта не установлена.
     */
    public String getCurrency() {
        return (String) session.getAttribute(CURRENCY_HEADER);
    }

    /**
     * Устанавливает текущую валюту для сеанса пользователя.
     *
     * @param currency Код валюты для установки.
     */
    public void setCurrency(String currency) {
        session.setAttribute(CURRENCY_HEADER, currency);
    }
}
