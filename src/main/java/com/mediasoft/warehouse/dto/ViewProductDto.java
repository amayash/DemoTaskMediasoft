package com.mediasoft.warehouse.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.ProductCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO для отображения {@link Product}.
 */
@Getter
@NoArgsConstructor
public class ViewProductDto {
    /**
     * Идентификатор товара.
     */
    private UUID id;

    /**
     * Наименование товара.
     */
    private String name;

    /**
     * Артикул товара.
     */
    private String article;

    /**
     * Описание товара.
     */
    private String description;

    /**
     * Категория товара.
     */
    private ProductCategory category;

    /**
     * Цена товара.
     */
    private BigDecimal price;

    /**
     * Количество товара.
     */
    private Long quantity;

    /**
     * Дата последнего изменения количества товара.
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastQuantityChangeDate;

    /**
     * Дата создания товара.
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdDate;

    /**
     * Создает экземпляр класса на основе объекта {@link Product}.
     *
     * @param product Объект {@link Product}, для которого создается DTO.
     */
    public ViewProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.article = product.getArticle();
        this.description = product.getDescription();
        this.category = product.getCategory();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.lastQuantityChangeDate = product.getLastQuantityChangeDate();
        this.createdDate = product.getCreatedDate();
    }
}
