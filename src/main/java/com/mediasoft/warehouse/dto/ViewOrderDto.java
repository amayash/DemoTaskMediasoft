package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Order;
import com.mediasoft.warehouse.model.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class ViewOrderDto {
    /**
     * Идентификатор заказа.
     */
    private UUID id;

    /**
     * Статус заказа.
     */
    private OrderStatus status;

    /**
     * Адрес для доставки заказа.
     */
    private String deliveryAddress;

    /**
     * Список товаров заказа.
     */
    private List<ViewOrderProductDto> products;

    /**
     * Идентификатор покупателя.
     */
    private Long customerId;

    public ViewOrderDto(Order order) {
        this.id = order.getId();
        this.status = order.getStatus();
        this.deliveryAddress = order.getDeliveryAddress();
        this.customerId = order.getCustomer().getId();
        if (order.getProducts() != null && !order.getProducts().isEmpty())
            this.products = order.getProducts()
                    .stream()
                    .map(x -> new ViewOrderProductDto(new ViewProductDto(x.getProduct()),
                            x.getId().getOrderId(), x.getQuantity(), x.getFrozenPrice())).toList();
    }
}
