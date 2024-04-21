package com.mediasoft.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductFilterDto {
    private String name;
    private BigDecimal price;
    private Integer quantity;
    @NotNull
    private Integer page;
    @NotNull
    private Integer size;
}
