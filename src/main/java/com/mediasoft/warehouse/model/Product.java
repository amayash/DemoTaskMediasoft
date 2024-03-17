package com.mediasoft.warehouse.model;

import com.mediasoft.warehouse.dto.SaveProductDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность, представляющая товар в системе.
 */
@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {
    /**
     * Идентификатор товара.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Название товара.
     */
    @Column(nullable = false)
    @NonNull
    private String name;

    /**
     * Артикул товара.
     */
    @Column(unique = true, nullable = false)
    @NonNull
    private String article;

    /**
     * Описание товара.
     */
    @Column(length = 512, nullable = false)
    @NonNull
    private String description;

    /**
     * Категория товара.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NonNull
    private ProductCategory category;

    /**
     * Цена товара.
     */
    @Column(nullable = false)
    @NonNull
    private Double price;

    /**
     * Количество товара.
     */
    @Column(nullable = false)
    @NonNull
    private Integer quantity;

    /**
     * Дата последнего изменения количества товара.
     */
    @Column(name = "last_quantity_change_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastQuantityChangeDate;

    /**
     * Дата создания товара.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", nullable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    /**
     * Устанавливает дату создания и последнего изменения количества перед созданием сущности.
     */
    @PrePersist
    void setDates() {
        this.createdDate = LocalDateTime.now();
        this.lastQuantityChangeDate = LocalDateTime.now();
    }

    /**
     * Конструктор, создающий объект класса на основе объекта {@link SaveProductDto}.
     *
     * @param saveProductDto Объект {@link SaveProductDto}, на основе которого создается товар.
     */
    public Product(SaveProductDto saveProductDto) {
        this.name = saveProductDto.getName();
        this.article = saveProductDto.getArticle();
        this.description = saveProductDto.getDescription();
        this.category = saveProductDto.getCategory();
        this.price = saveProductDto.getPrice();
        this.quantity = saveProductDto.getQuantity();
    }
}
