package com.mediasoft.warehouse.filter.currency;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для обработки валюты в запросах.
 */
@Component
@RequiredArgsConstructor
public class CurrencyFilter extends OncePerRequestFilter {
    private static final String CURRENCY_HEADER = CurrencyProvider.CURRENCY_HEADER;
    private static final String DEFAULT_CURRENCY = CurrencyProvider.DEFAULT_CURRENCY;
    private final CurrencyProvider currencyProvider;

    /**
     * Обрабатывает запросы, устанавливая или получая валюту в зависимости от заголовка запроса.
     *
     * @param request     HTTP-запрос.
     * @param response    HTTP-ответ.
     * @param filterChain Объект для цепочки фильтров.
     * @throws ServletException если происходит ошибка сервлета.
     * @throws IOException      если происходит ошибка ввода-вывода.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String currency = request.getHeader(CURRENCY_HEADER);
        if (currency == null || currency.isEmpty()) {
            currency = currencyProvider.getCurrency();
            if (currency == null || currency.isEmpty()) {
                currency = DEFAULT_CURRENCY;
            }
        }
        currencyProvider.setCurrency(currency);
        filterChain.doFilter(request, response);
    }
}
