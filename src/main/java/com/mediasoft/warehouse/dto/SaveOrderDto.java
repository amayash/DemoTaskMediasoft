package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO для сохранения или изменения {@link Order}.
 */
@Getter
@Setter
@NoArgsConstructor
public class SaveOrderDto {
    /**
     * Адрес для доставки заказа.
     */
    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    /**
     * Продукты заказа.
     */
    @NotEmpty(message = "Products are required")
    private List<@Valid SaveOrderProductDto> products;
}
