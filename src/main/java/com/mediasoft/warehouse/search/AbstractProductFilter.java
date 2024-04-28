package com.mediasoft.warehouse.search;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.FieldName;
import com.mediasoft.warehouse.model.enums.OperationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

/**
 * Абстрактный класс для фильтрации товаров.
 * Определяет общие атрибуты и методы для всех типов фильтров.
 *
 * @param <T> тип параметра поиска
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        visible = true,
        property = "field"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UUIDProductFilter.class, name = "ID"),
        @JsonSubTypes.Type(value = StringProductFilter.class, name = "NAME"),
        @JsonSubTypes.Type(value = StringProductFilter.class, name = "ARTICLE"),
        @JsonSubTypes.Type(value = StringProductFilter.class, name = "DESCRIPTION"),
        @JsonSubTypes.Type(value = StringProductFilter.class, name = "CATEGORY"),
        @JsonSubTypes.Type(value = BigDecimalProductFilter.class, name = "PRICE"),
        @JsonSubTypes.Type(value = LongProductFilter.class, name = "QUANTITY"),
        @JsonSubTypes.Type(value = LocalDateTimeProductFilter.class, name = "LAST_QUANTITY_CHANGE_DATE"),
        @JsonSubTypes.Type(value = LocalDateProductFilter.class, name = "CREATED_DATE")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractProductFilter<T> {
    /**
     * Поле, по которому производится фильтрация.
     */
    @NotNull
    protected FieldName field;

    /**
     * Операция фильтрации.
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    protected OperationType operation;

    /**
     * Параметр поиска.
     */
    @NotNull
    protected T searchParam;

    /**
     * Метод, возвращающий спецификацию для операции "~".
     *
     * @return спецификация для операции "~"
     */
    abstract public Specification<Product> likeOperation();

    /**
     * Метод, возвращающий спецификацию для операции "=".
     *
     * @return спецификация для операции "="
     */
    abstract public Specification<Product> equalsOperation();

    /**
     * Метод, возвращающий спецификацию для операции ">=".
     *
     * @return спецификация для операции ">="
     */
    abstract public Specification<Product> greaterThanOrEqualsOperation();

    /**
     * Метод, возвращающий спецификацию для операции "<=".
     *
     * @return спецификация для операции "<="
     */
    abstract public Specification<Product> lessThanOrEqualsOperation();
}
