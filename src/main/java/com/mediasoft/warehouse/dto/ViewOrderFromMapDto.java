package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Order;
import com.mediasoft.warehouse.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO для отображения {@link Order}.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewOrderFromMapDto {
    /**
     * Идентификатор заказа.
     */
    private UUID id;
    /**
     * Покупатель заказа.
     */
    private ViewCustomerFromOrderDto customer;
    /**
     * Статус заказа.
     */
    private OrderStatus status;
    /**
     * Адрес заказа.
     */
    private String deliveryAddress;
    /**
     * Количество товаров заказа.
     */
    private Long quantity;
}
