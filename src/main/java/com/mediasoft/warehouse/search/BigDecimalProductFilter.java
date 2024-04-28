package com.mediasoft.warehouse.search;

import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Фильтр товаров по числовым значениям.
 * Определяет спецификации для различных операций фильтрации.
 */
public class BigDecimalProductFilter extends AbstractProductFilter<BigDecimal> {
    /**
     * Возвращает спецификацию для операции "~".
     *
     * @return спецификация для операции "~"
     */
    @Override
    public Specification<Product> likeOperation() {
        return (root, query, criteriaBuilder) -> {
            BigDecimal tenPercent = searchParam.multiply(BigDecimal.valueOf(0.1));
            BigDecimal lowerBound = searchParam.subtract(tenPercent);
            BigDecimal upperBound = searchParam.add(tenPercent);
            return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
        };
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
                criteriaBuilder.greaterThanOrEqualTo(root.get(field.name().toLowerCase()), searchParam);
    }

    /**
     * Возвращает спецификацию для операции "<=".
     *
     * @return спецификация для операции "<="
     */
    @Override
    public Specification<Product> lessThanOrEqualsOperation() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(field.name().toLowerCase()), searchParam);
    }
}