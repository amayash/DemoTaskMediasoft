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
    UUID id;
    /**
     * Покупатель заказа.
     */
    ViewCustomerFromOrderDto customer;
    /**
     * Статус заказа.
     */
    OrderStatus status;
    /**
     * Адрес заказа.
     */
    String deliveryAddress;
    /**
     * Количество товаров заказа.
     */
    Long quantity;
}
