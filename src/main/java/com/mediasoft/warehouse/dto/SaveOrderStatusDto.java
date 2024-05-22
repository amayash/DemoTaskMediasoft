package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Order;
import com.mediasoft.warehouse.model.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для сохранения или изменения статуса заказа {@link Order}.
 */
@Getter
@Setter
@NoArgsConstructor
public class SaveOrderStatusDto {
    /**
     * Статус заказа.
     */
    private OrderStatus status;
}
