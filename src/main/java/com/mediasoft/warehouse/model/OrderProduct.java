package com.mediasoft.warehouse.model;

import jakarta.persistence.*;
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

    /**
     * Конструктор для создания объекта OrderProduct.
     *
     * @param order    Заказ, к которому относится данный товар.
     * @param product  Товар, который является частью данного заказа.
     * @param quantity Количество данного товара в заказе.
     */
    public OrderProduct(Order order, Product product, Long quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.frozenPrice = product.getPrice();
        this.id = new OrderProductKey(order.getId(), product.getId());
    }
}
