package com.mediasoft.warehouse.search;

import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.domain.Specification;

/**
 * Фильтр товаров по Long значениям.
 * Определяет спецификации для различных операций фильтрации.
 */
public class LongProductFilter extends AbstractProductFilter<Long> {
    /**
     * Возвращает спецификацию для операции "~".
     *
     * @return спецификация для операции "~"
     */
    @Override
    public Specification<Product> likeOperation() {
        return (root, query, criteriaBuilder) -> {
            long tenPercent = (long) (searchParam * 0.1);
            long lowerBound = searchParam - tenPercent;
            long upperBound = searchParam + tenPercent;
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
