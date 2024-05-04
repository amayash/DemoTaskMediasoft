package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Customer;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для сохранения или изменения {@link Customer}.
 */
@Getter
@Setter
@NoArgsConstructor
public class SaveCustomerDto {
    /**
     * Логин покупателя.
     */
    @NotBlank(message = "Login is required")
    private String login;

    /**
     * Почта покупателя.
     */
    @NotBlank(message = "Email is required")
    private String email;
}
