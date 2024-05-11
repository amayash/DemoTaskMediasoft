package com.mediasoft.warehouse.dto;

import lombok.*;

import java.math.BigDecimal;

/**
 * Класс, представляющий объект данных о курсах валют.
 */
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViewCurrenciesDto {
    /**
     * Курс китайского юаня.
     */
    private BigDecimal CNY;
    /**
     * Курс доллара США.
     */
    private BigDecimal USD;
    /**
     * Курс евро.
     */
    private BigDecimal EUR;
}