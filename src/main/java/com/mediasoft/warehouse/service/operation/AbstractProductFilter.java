package com.mediasoft.warehouse.service.operation;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.FieldName;
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
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StringProductFilter.class, name = "string"),
        @JsonSubTypes.Type(value = NumberProductFilter.class, name = "number")
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
    protected String operation;

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
