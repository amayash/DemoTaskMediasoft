package com.mediasoft.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * DTO для списка товаров в заказе {@link SaveOrderDto}.
 */
@Getter
@Setter
@NoArgsConstructor
public class SaveOrderProductDto {
    /**
     * Идентификатор товара.
     */
    @NotNull(message = "Id is required")
    private UUID id;
    /**
     * Количество товара.
     */
    @NotNull(message = "Quantity is required")
    @PositiveOrZero
    private Long quantity;
}
