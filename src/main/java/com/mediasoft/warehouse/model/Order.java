package com.mediasoft.warehouse.model;

import com.mediasoft.warehouse.dto.SaveOrderDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сущность, представляющая заказ в системе.
 */
@Entity(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    /**
     * Идентификатор заказа.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private UUID id;

    /**
     * Статус заказа.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NonNull
    private OrderStatus status;

    /**
     * Адрес для доставки заказа.
     */
    @Column(name = "delivery_address", nullable = false)
    @NonNull
    private String deliveryAddress;

    /**
     * Список товаров заказа.
     */
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade =
            {
                    CascadeType.REMOVE,
                    CascadeType.MERGE,
                    CascadeType.PERSIST
            }, orphanRemoval = true)
    private List<OrderProduct> products;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    /**
     * Добавление товара в заказ.
     */
    public void addProduct(OrderProduct orderProduct) {
        if (products == null) {
            products = new ArrayList<>();
        }
        if (!products.contains(orderProduct))
            this.products.add(orderProduct);
    }

    /**
     * Конструктор, создающий объект класса на основе объекта {@link SaveOrderDto}.
     *
     * @param saveOrderDto Объект {@link SaveOrderDto}, на основе которого создается заказ.
     */
    public Order(SaveOrderDto saveOrderDto) {
        this.status = OrderStatus.CREATED;
        this.deliveryAddress = saveOrderDto.getDeliveryAddress();
        this.products = new ArrayList<>();
    }
}
