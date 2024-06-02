package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO для отображения {@link Customer}.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewCustomerFromOrderDto {
    /**
     * Идентификатор покупателя.
     */
    private Long id;
    /**
     * Счет покупателя.
     */
    private String accountNumber;
    /**
     * Почта покупателя.
     */
    private String email;
    /**
     * ИНН покупателя.
     */
    private String crm;
}
