package com.mediasoft.warehouse.search;

import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LocalDateProductFilter extends AbstractProductFilter<LocalDate> {
    /**
     * Возвращает спецификацию для операции "~".
     *
     * @return спецификация для операции "~"
     */
    @Override
    public Specification<Product> likeOperation() {
        return (root, query, criteriaBuilder) -> {
            LocalDate lowerBound = searchParam.minusDays(3);
            LocalDate upperBound = searchParam.plusDays(3);
            return criteriaBuilder.between(root.get("createdDate"), lowerBound, upperBound);
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
                criteriaBuilder.equal(root.get("createdDate"), searchParam);
    }

    /**
     * Возвращает спецификацию для операции ">=".
     *
     * @return спецификация для операции ">="
     */
    @Override
    public Specification<Product> greaterThanOrEqualsOperation() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), searchParam);
    }

    /**
     * Возвращает спецификацию для операции "<=".
     *
     * @return спецификация для операции "<="
     */
    @Override
    public Specification<Product> lessThanOrEqualsOperation() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), searchParam);
    }
}
