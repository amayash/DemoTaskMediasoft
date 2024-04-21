package com.mediasoft.warehouse.model;

import com.mediasoft.warehouse.dto.SaveProductDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность, представляющая товар в системе.
 */
@Entity(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    /**
     * Идентификатор товара.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private UUID id;

    /**
     * Название товара.
     */
    @Column(name = "name", nullable = false)
    @NonNull
    private String name;

    /**
     * Артикул товара.
     */
    @Column(name = "article", unique = true, nullable = false)
    @NonNull
    private String article;

    /**
     * Описание товара.
     */
    @Column(name = "description", length = 512, nullable = false)
    @NonNull
    private String description;

    /**
     * Категория товара.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    @NonNull
    private ProductCategory category;

    /**
     * Цена товара.
     */
    @Column(name = "price", nullable = false)
    @NonNull
    private BigDecimal price;

    /**
     * Количество товара.
     */
    @Column(name = "quantity", nullable = false)
    @NonNull
    private Long quantity;

    /**
     * Дата последнего изменения количества товара.
     */
    @CreationTimestamp
    @Column(name = "last_quantity_change_date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastQuantityChangeDate;

    /**
     * Дата создания товара.
     */
    @Column(name = "created_date", updatable = false, nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")

    private LocalDate createdDate;

    /**
     * Устанавливает дату создания перед созданием сущности.
     */
    @PrePersist
    void setCreatedDate() {
        this.createdDate = LocalDate.now();
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
