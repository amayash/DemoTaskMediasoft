package com.mediasoft.warehouse.search;

import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

/**
 * Фильтр товаров по UUID значениям.
 * Определяет спецификации для различных операций фильтрации.
 */
public class UUIDProductFilter extends AbstractProductFilter<UUID> {
    /**
     * Возвращает спецификацию для операции "~".
     *
     * @return спецификация для операции "~"
     */
    @Override
    public Specification<Product> likeOperation() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), "%" + searchParam + "%");
    }

    /**
     * Возвращает спецификацию для операции "=".
     *
     * @return спецификация для операции "="
     */
    @Override
    public Specification<Product> equalsOperation() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(field.name().toLowerCase()), searchParam);
    }

    /**
     * Возвращает спецификацию для операции ">=".
     *
     * @return спецификация для операции ">="
     */
    @Override
    public Specification<Product> greaterThanOrEqualsOperation() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), searchParam + "%");
    }

    /**
     * Возвращает спецификацию для операции "<=".
     *
     * @return спецификация для операции "<="
     */
    @Override
    public Specification<Product> lessThanOrEqualsOperation() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), "%" + searchParam);
    }
}
