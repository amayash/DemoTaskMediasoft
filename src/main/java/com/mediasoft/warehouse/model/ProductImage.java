package com.mediasoft.warehouse.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

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
     * Идентификатор изображения.
     */
    @Id
    @Column(name = "s3_key", updatable = false, unique = true, nullable = false)
    private UUID s3_key;

    /**
     * Идентификатор товара.
     */
    @Column(name = "product_id")
    @NonNull
    private UUID productId;
}
