package com.mediasoft.warehouse.filter.currency;

import com.mediasoft.warehouse.model.enums.Currency;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * Фильтр для обработки валюты в запросах.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyFilter extends OncePerRequestFilter {
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
        String currency = request.getHeader("currency");
        Optional.ofNullable(currency)
                .map(Currency::fromValue)
                .ifPresent(currencyProvider::setCurrency);
        filterChain.doFilter(request, response);
    }
}
