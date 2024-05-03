package com.mediasoft.warehouse.dto;

import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.ProductCategory;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO для сохранения или изменения {@link Product}.
 */
@Getter
@Setter
@NoArgsConstructor
public class SaveProductDto {
    /**
     * Наименование товара.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 255)
    private String name;

    /**
     * Артикул товара.
     */
    @NotBlank(message = "Article is required")
    @Size(min = 3, max = 255)
    private String article;

    /**
     * Описание товара.
     */
    @NotBlank(message = "Description is required")
    @Size(min = 3, max = 512)
    private String description;

    /**
     * Категория товара.
     */
    @NotNull(message = "Category is required")
    private ProductCategory category;

    /**
     * Цена товара.
     */
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private BigDecimal price;

    /**
     * Количество товара.
     */
    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be a positive number")
    private Long quantity;

    public SaveProductDto(Product product) {
        this.name = product.getName();
        this.article = product.getArticle();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
    }
}
