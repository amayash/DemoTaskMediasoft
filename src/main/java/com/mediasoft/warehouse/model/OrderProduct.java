package com.mediasoft.warehouse.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "order_product")
@NoArgsConstructor
@Getter
@Setter
public class OrderProduct {
    @EmbeddedId
    private OrderProductKey id;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "frozen_price", nullable = false)
    private BigDecimal frozenPrice;

    public OrderProduct(Order order, Product product, Long quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.frozenPrice = product.getPrice();
        this.id = new OrderProductKey(order.getId(), product.getId());
    }
}
