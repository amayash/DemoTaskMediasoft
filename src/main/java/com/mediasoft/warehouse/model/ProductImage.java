package com.mediasoft.warehouse.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность, представляющая изображение товара в системе.
 */
@Entity
@Table(name = "product_images")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductImage {
    /**
     * Идентификатор, состоящий из идентификаторов изображения и товара.
     */
    @EmbeddedId
    private ProductImageKey id;
}
