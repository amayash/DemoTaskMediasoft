package com.mediasoft.warehouse.search;

import com.mediasoft.warehouse.error.exception.CompareWithStringException;
import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.domain.Specification;

/**
 * Фильтр товаров по строковым значениям.
 * Определяет спецификации для различных операций фильтрации.
 */
public class StringProductFilter extends AbstractProductFilter<String> {
    /**
     * Возвращает спецификацию для операции "~".
     *
     * @return спецификация для операции "~"
     */
    @Override
    public Specification<Product> likeOperation() {
        return switch (field) {
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), "%" + searchParam + "%");

            default -> throw new IllegalArgumentException("Can't compare string to " + field.name());
        };
    }

    /**
     * Возвращает спецификацию для операции "=".
     *
     * @return спецификация для операции "="
     */
    @Override
    public Specification<Product> equalsOperation() {
        return switch (field) {
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), searchParam);
            default -> throw new CompareWithStringException(field.name());
        };
    }

    /**
     * Возвращает спецификацию для операции ">=".
     *
     * @return спецификация для операции ">="
     */
    @Override
    public Specification<Product> greaterThanOrEqualsOperation() {
        return switch (field) {
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), searchParam + "%");
            default -> throw new CompareWithStringException(field.name());
        };
    }

    /**
     * Возвращает спецификацию для операции "<=".
     *
     * @return спецификация для операции "<="
     */
    @Override
    public Specification<Product> lessThanOrEqualsOperation() {
        return switch (field) {
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), "%" + searchParam);
            default -> throw new CompareWithStringException(field.name());
        };
    }
}