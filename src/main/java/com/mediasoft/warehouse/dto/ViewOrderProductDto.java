package com.mediasoft.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO для отображения товаров в заказе {@link ViewOrderDto}.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewOrderProductDto {
    /**
     * Идентификатор товара.
     */
    private UUID id;

    /**
     * Название товара.
     */
    private String name;

    /**
     * Количество товара.
     */
    private Long quantity;

    /**
     * Цена товара.
     */
    private BigDecimal frozenPrice;
}
