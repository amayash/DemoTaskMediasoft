package com.mediasoft.warehouse.model;

import com.mediasoft.warehouse.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;
import java.util.UUID;

/**
 * Сущность, представляющая заказ в системе.
 */
@Entity
@Table(name = "orders")
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
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderProduct> products;

    /**
     * Покупатель заказа.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Customer customer;
}
