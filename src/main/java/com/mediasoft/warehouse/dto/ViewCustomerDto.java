package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Customer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для сохранения или изменения {@link Customer}.
 */
@Getter
@Setter
@NoArgsConstructor
public class ViewCustomerDto {
    /**
     * Идентификатор покупателя.
     */
    private Long id;

    /**
     * Логин покупателя.
     */
    private String login;

    /**
     * Почта покупателя.
     */
    private String email;

    /**
     * Отметка об активности покупателя.
     */
    private Boolean isActive;

    public ViewCustomerDto(Customer customer) {
        this.id = customer.getId();
        this.login = customer.getLogin();
        this.email = customer.getEmail();
        this.isActive = customer.getIsActive();
    }
}
