package com.mediasoft.warehouse.service.operation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.FieldName;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@JsonTypeName("String")
public class OperationStringImpl implements IOperation<String, Product> {
    @Override
    public Specification<Product> likeOperation(String searchParam, FieldName field) {
        return switch (field) {
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), "%" + searchParam + "%");
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), "%" + searchParam + "%");
            case PRICE -> (root, query, criteriaBuilder) -> {
                BigDecimal searchValue = new BigDecimal(searchParam);
                BigDecimal lowerBound = searchValue.subtract(BigDecimal.TEN);
                BigDecimal upperBound = searchValue.add(BigDecimal.TEN);
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
            case QUANTITY -> (root, query, criteriaBuilder) -> {
                long searchValue = Long.parseLong(searchParam);
                Long lowerBound = searchValue - 5;
                Long upperBound = searchValue + 5;
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDateTime searchValue = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                LocalDateTime lowerBound = searchValue.minusDays(3);
                LocalDateTime upperBound = searchValue.plusDays(3);
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate lowerBound = searchValue.minusDays(3);
                LocalDate upperBound = searchValue.plusDays(3);
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
        };
    }

    @Override
    public Specification<Product> equalsOperation(String searchParam, FieldName field) {
        return switch (field) {
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), UUID.fromString(searchParam));
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), searchParam);
            case PRICE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), new BigDecimal(searchParam));
            case QUANTITY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), Long.parseLong(searchParam));
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchDate = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate();
                return criteriaBuilder.equal(root.get(field.name().toLowerCase()).as(LocalDate.class), searchDate);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                return criteriaBuilder.equal(root.get(field.name().toLowerCase()), searchValue);
            };
        };
    }

    @Override
    public Specification<Product> greaterThanOrEqualsOperation(String searchParam, FieldName field) {
        return switch (field) {
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), searchParam + "%");
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), searchParam + "%");
            case PRICE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(field.name().toLowerCase()), new BigDecimal(searchParam));
            case QUANTITY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(field.name().toLowerCase()), Long.parseLong(searchParam));
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDateTime searchValue = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return criteriaBuilder.greaterThanOrEqualTo(root.get(field.name()), searchValue);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                return criteriaBuilder.greaterThanOrEqualTo(root.get(field.name()), searchValue);
            };
        };
    }

    @Override
    public Specification<Product> lessThanOrEqualsOperation(String searchParam, FieldName field) {
        return switch (field) {
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), "%" + searchParam);
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), "%" + searchParam);
            case PRICE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(field.name().toLowerCase()), new BigDecimal(searchParam));
            case QUANTITY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(field.name().toLowerCase()), Long.parseLong(searchParam));
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDateTime searchValue = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return criteriaBuilder.lessThanOrEqualTo(root.get(field.name()), searchValue);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                return criteriaBuilder.greaterThanOrEqualTo(root.get(field.name()), searchValue);
            };
        };
    }
}
