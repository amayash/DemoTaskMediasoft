package com.mediasoft.warehouse.model;

import com.mediasoft.warehouse.model.enums.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
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
    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderProduct> products;

    /**
     * Покупатель заказа.
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @Fetch(FetchMode.JOIN)
    private Customer customer;

    @Column(name = "business_key")
    private String businessKey;
}
