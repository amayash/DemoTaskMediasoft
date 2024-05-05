package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Order;
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

    public ViewOrderDto(Order order) {
        this.id = order.getId();
        this.totalPrice = BigDecimal.ZERO;
        if (order.getProducts() != null && !order.getProducts().isEmpty())
            this.products = order.getProducts()
                    .stream()
                    .map(x -> {
                        totalPrice = totalPrice.add(x.getFrozenPrice().multiply(BigDecimal.valueOf(x.getQuantity())));
                        return new ViewOrderProductDto(
                                x.getProduct().getId(),
                                x.getProduct().getName(),
                                x.getQuantity(),
                                x.getFrozenPrice());
                    }).toList();
    }
}
