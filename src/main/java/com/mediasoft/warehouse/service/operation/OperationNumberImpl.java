package com.mediasoft.warehouse.service.operation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.FieldName;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

@JsonTypeName("Number")
public class OperationNumberImpl implements IOperation<BigDecimal, Product> {
    @Override
    public Specification<Product> likeOperation(BigDecimal searchParam, FieldName field) {
        return switch (field) {
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class),
                            "%" + searchParam.stripTrailingZeros().toPlainString() + "%");
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()),
                            "%" + searchParam.stripTrailingZeros().toPlainString() + "%");
            case PRICE -> (root, query, criteriaBuilder) -> {
                BigDecimal lowerBound = searchParam.subtract(BigDecimal.TEN);
                BigDecimal upperBound = searchParam.add(BigDecimal.TEN);
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
            case QUANTITY -> (root, query, criteriaBuilder) -> {
                long searchValue = searchParam.longValue();
                Long lowerBound = searchValue - 5;
                Long upperBound = searchValue + 5;
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
            default -> throw new IllegalArgumentException("Can't convert number to " + field.name());
        };
    }

    @Override
    public Specification<Product> equalsOperation(BigDecimal searchParam, FieldName field) {
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

    @Override
    public Specification<Product> greaterThanOrEqualsOperation(BigDecimal searchParam, FieldName field) {
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

    @Override
    public Specification<Product> lessThanOrEqualsOperation(BigDecimal searchParam, FieldName field) {
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
