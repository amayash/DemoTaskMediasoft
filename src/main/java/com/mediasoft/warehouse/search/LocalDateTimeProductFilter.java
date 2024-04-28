package com.mediasoft.warehouse.search;

import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class LocalDateTimeProductFilter extends AbstractProductFilter<LocalDateTime> {
    /**
     * Возвращает спецификацию для операции "~".
     *
     * @return спецификация для операции "~"
     */
    @Override
    public Specification<Product> likeOperation() {
        return (root, query, criteriaBuilder) -> {
            LocalDateTime lowerBound = searchParam.minusDays(3);
            LocalDateTime upperBound = searchParam.plusDays(3);
            return criteriaBuilder.between(root.get("lastQuantityChangeDate"), lowerBound, upperBound);
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
                criteriaBuilder.equal(root.get("lastQuantityChangeDate").as(LocalDate.class), searchParam);
    }

    /**
     * Возвращает спецификацию для операции ">=".
     *
     * @return спецификация для операции ">="
     */
    @Override
    public Specification<Product> greaterThanOrEqualsOperation() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("lastQuantityChangeDate"), searchParam);
    }

    /**
     * Возвращает спецификацию для операции "<=".
     *
     * @return спецификация для операции "<="
     */
    @Override
    public Specification<Product> lessThanOrEqualsOperation() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("lastQuantityChangeDate"), searchParam);
    }
}
