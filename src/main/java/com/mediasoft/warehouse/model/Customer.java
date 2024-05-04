package com.mediasoft.warehouse.model;

import com.mediasoft.warehouse.dto.SaveCustomerDto;
import com.mediasoft.warehouse.dto.SaveProductDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Сущность, представляющая покупателя в системе.
 */
@Entity(name = "customers")
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

    /**
     * Конструктор, создающий объект класса на основе объекта {@link SaveCustomerDto}.
     *
     * @param saveProductDto Объект {@link SaveProductDto}, на основе которого создается покупатель.
     */
    public Customer(SaveCustomerDto saveProductDto) {
        this.login = saveProductDto.getLogin();
        this.email = saveProductDto.getEmail();
        this.isActive = true;
        this.orders = new ArrayList<>();
    }
}
