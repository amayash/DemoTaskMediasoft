package com.mediasoft.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewOrderProductDto {
    private ViewProductDto product;
    private UUID orderId;
    private Long quantity;
    private BigDecimal frozenPrice;
}
