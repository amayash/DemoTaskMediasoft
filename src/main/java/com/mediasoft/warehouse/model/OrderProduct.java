package com.mediasoft.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Сущность, представляющая связь Заказ_Товар в системе.
 */
@Entity
@Table(name = "order_product")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class OrderProduct {
    /**
     * Идентификатор, состоящий из идентификаторов заказа и товара.
     */
    @EmbeddedId
    private OrderProductKey id;

    /**
     * Заказ, к которому относится данный товар.
     */
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Товар, который является частью данного заказа.
     */
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Количество данного товара в заказе.
     */
    @Column(name = "quantity", nullable = false)
    private Long quantity;

    /**
     * Замороженная цена товара на момент добавления в заказ.
     */
    @Column(name = "frozen_price", nullable = false)
    private BigDecimal frozenPrice;
}
