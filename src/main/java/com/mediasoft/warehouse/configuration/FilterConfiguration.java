package com.mediasoft.warehouse.configuration;

import com.mediasoft.warehouse.filter.currency.CurrencyFilter;
import com.mediasoft.warehouse.filter.currency.CurrencyProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для регистрации фильтра {@link CurrencyFilter} в приложении.
 */
@Configuration
public class FilterConfiguration {

    /**
     * Создает и регистрирует бин для фильтра валюты.
     *
     * @param currencyProvider Провайдер валюты, используемый фильтром.
     * @return Объект {@link FilterRegistrationBean}, представляющий зарегистрированный фильтр.
     */
    @Bean
    public FilterRegistrationBean<CurrencyFilter> currencyFilterRegistrationBean(CurrencyProvider currencyProvider) {
        FilterRegistrationBean<CurrencyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CurrencyFilter(currencyProvider));
        registrationBean.addUrlPatterns("/products", "/products/search", "/products/*");
        return registrationBean;
    }
}
