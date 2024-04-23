package com.mediasoft.warehouse.service.operation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

/**
 * Фильтр товаров по числовым значениям.
 * Определяет спецификации для различных операций фильтрации.
 */
@JsonTypeName("number")
public class NumberProductFilter extends AbstractProductFilter<BigDecimal> {
    /**
     * Возвращает спецификацию для операции "~".
     *
     * @return спецификация для операции "~"
     */
    @Override
    public Specification<Product> likeOperation() {
        return switch (field) {
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class),
                            "%" + searchParam.stripTrailingZeros().toPlainString() + "%");
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()),
                            "%" + searchParam.stripTrailingZeros().toPlainString() + "%");
            case PRICE -> (root, query, criteriaBuilder) -> {
                BigDecimal tenPercent = searchParam.multiply(BigDecimal.valueOf(0.1));
                BigDecimal lowerBound = searchParam.subtract(tenPercent);
                BigDecimal upperBound = searchParam.add(tenPercent);
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
            case QUANTITY -> (root, query, criteriaBuilder) -> {
                long searchValue = searchParam.longValue();
                long tenPercent = (long) (searchValue * 0.1);
                long lowerBound = searchValue - tenPercent;
                long upperBound = searchValue + tenPercent;
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
            default -> throw new IllegalArgumentException("Can't convert number to " + field.name());
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
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()).as(String.class),
                            searchParam.stripTrailingZeros().toPlainString());
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), searchParam.stripTrailingZeros().toPlainString());
            case PRICE ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field.name().toLowerCase()), searchParam);
            case QUANTITY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), searchParam.longValue());
            default -> throw new IllegalArgumentException("Can't convert number to " + field.name());
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
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class),
                            searchParam.stripTrailingZeros().toPlainString() + "%");
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), searchParam.stripTrailingZeros().toPlainString() + "%");
            case PRICE ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(field.name().toLowerCase()), searchParam);
            case QUANTITY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(field.name().toLowerCase()), searchParam.longValue());
            default -> throw new IllegalArgumentException("Can't convert number to " + field.name());
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
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class),
                            "%" + searchParam.stripTrailingZeros().toPlainString());
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), "%" + searchParam.stripTrailingZeros().toPlainString());
            case PRICE ->
                    (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(field.name().toLowerCase()), searchParam);
            case QUANTITY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(field.name().toLowerCase()), searchParam.longValue());
            default -> throw new IllegalArgumentException("Can't convert number to " + field.name());
        };
    }
}