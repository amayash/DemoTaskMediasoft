package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.enums.FieldName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductFilterDto<T> {
    private FieldName field;
    private T value;
    private String operation;
}
