package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO для принятия {@link Order}.
 */
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmOrderDto {
    /**
     * Идентификатор заказа.
     */
    @NotNull(message = "Order id is required")
    private UUID orderId;
    /**
     * Адрес доставки заказа.
     */

    @NotBlank(message = "Delivery address is required")
    private String orderDeliveryAddress;

    /**
     * ИНН покупателя.
     */
    @NotBlank(message = "Customer CRM is required")
    private String customerCRM;

    /**
     * Номер счета покупателя.
     */
    @NotBlank(message = "Customer account number is required")
    private String customerAccountNumber;

    /**
     * Стоимость заказа.
     */
    @NotNull(message = "Order price is required")
    private BigDecimal orderPrice;

    /**
     * Логин покупателя.
     */
    @NotBlank(message = "Customer login date is required")
    private String customerLogin;
}
