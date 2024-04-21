package com.mediasoft.warehouse.scheduling;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Интерфейс для планировщика увеличения цен.
 */
public interface PriceScheduler {
    /**
     * Основной метод планировщика, который будет отрабатывать по таймеру.
     */
    void scheduleFixedDelayTask();

    /**
     * Метод для получения новой цены товара в зависимости от указанного процента
     *
     * @param oldPrice Предыдущая цена товара.
     * @param percent  Процент, на который цена товара увеличится.
     * @return Новая цена товара.
     */
    default BigDecimal getNewPrice(BigDecimal oldPrice, BigDecimal percent) {
        BigDecimal percentage = percent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        BigDecimal increase = oldPrice.multiply(percentage);
        return oldPrice.add(increase);
    }
}
