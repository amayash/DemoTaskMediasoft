package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO для отображения {@link Order}.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewOrderDto {
    /**
     * Идентификатор заказа.
     */
    private UUID id;

    /**
     * Список товаров заказа.
     */
    private List<ViewOrderProductDto> products;

    /**
     * Итоговая сумма заказа.
     */
    private BigDecimal totalPrice;
}
