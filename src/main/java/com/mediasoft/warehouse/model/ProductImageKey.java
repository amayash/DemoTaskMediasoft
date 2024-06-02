package com.mediasoft.warehouse.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Композитный ключ для сущности {@link ProductImage}.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImageKey implements Serializable {
    /**
     * Идентификатор изображения.
     */
    private String s3_key;

    /**
     * Идентификатор товара.
     */
    private UUID productId;
}
