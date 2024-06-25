package com.mediasoft.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO для сохранения даты доставки заказа {@link com.mediasoft.warehouse.model.Order}.
 */
@Getter
@Setter
@NoArgsConstructor
public class SaveOrderDeliveryDateDto {
    /**
     * Дата доставки заказа.
     */
    @NotNull(message = "Delivery date is required")
    private LocalDate deliveryDate;
}
