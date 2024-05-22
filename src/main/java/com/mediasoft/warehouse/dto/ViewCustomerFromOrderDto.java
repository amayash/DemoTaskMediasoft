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
    Long id;
    /**
     * Счет покупателя.
     */
    String accountNumber;
    /**
     * Почта покупателя.
     */
    String email;
    /**
     * ИНН покупателя.
     */
    String crm;
}
