package com.mediasoft.warehouse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Сущность, представляющая покупателя в системе.
 */
@Entity
@Table(name = "customer")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {
    /**
     * Идентификатор покупателя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private Long id;

    /**
     * Логин покупателя.
     */
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    /**
     * Почта покупателя.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * Отметка об активности покупателя.
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    /**
     * Список заказов покупателя.
     */
    @OneToMany(mappedBy = "customer")
    private List<Order> orders;
}
