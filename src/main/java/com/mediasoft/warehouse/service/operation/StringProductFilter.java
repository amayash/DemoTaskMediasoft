package com.mediasoft.warehouse.service.operation;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Фильтр товаров по строковым значениям.
 * Определяет спецификации для различных операций фильтрации.
 */
@JsonTypeName("string")
public class StringProductFilter extends AbstractProductFilter<String> {
    /**
     * Возвращает спецификацию для операции "~".
     *
     * @return спецификация для операции "~"
     */
    @Override
    public Specification<Product> likeOperation() {
        return switch (field) {
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), "%" + searchParam + "%");
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), "%" + searchParam + "%");
            case PRICE -> (root, query, criteriaBuilder) -> {
                BigDecimal searchValue = new BigDecimal(searchParam);
                BigDecimal tenPercent = searchValue.multiply(BigDecimal.valueOf(0.1));
                BigDecimal lowerBound = searchValue.subtract(tenPercent);
                BigDecimal upperBound = searchValue.add(tenPercent);
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
            case QUANTITY -> (root, query, criteriaBuilder) -> {
                long searchValue = Long.parseLong(searchParam);
                long tenPercent = (long) (searchValue * 0.1);
                long lowerBound = searchValue - tenPercent;
                long upperBound = searchValue + tenPercent;
                return criteriaBuilder.between(root.get(field.name().toLowerCase()), lowerBound, upperBound);
            };
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDateTime searchValue = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                LocalDateTime lowerBound = searchValue.minusDays(3);
                LocalDateTime upperBound = searchValue.plusDays(3);
                return criteriaBuilder.between(root.get("lastQuantityChangeDate"), lowerBound, upperBound);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate lowerBound = searchValue.minusDays(3);
                LocalDate upperBound = searchValue.plusDays(3);
                return criteriaBuilder.between(root.get("createdDate"), lowerBound, upperBound);
            };
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
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), UUID.fromString(searchParam));
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), searchParam);
            case PRICE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), new BigDecimal(searchParam));
            case QUANTITY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), Long.parseLong(searchParam));
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchDate = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate();
                return criteriaBuilder.equal(root.get("lastQuantityChangeDate").as(LocalDate.class), searchDate);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                return criteriaBuilder.equal(root.get("createdDate"), searchValue);
            };
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
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), searchParam + "%");
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), searchParam + "%");
            case PRICE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(field.name().toLowerCase()), new BigDecimal(searchParam));
            case QUANTITY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get(field.name().toLowerCase()), Long.parseLong(searchParam));
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDateTime searchValue = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("lastQuantityChangeDate"), searchValue);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), searchValue);
            };
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
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), "%" + searchParam);
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), "%" + searchParam);
            case PRICE -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(field.name().toLowerCase()), new BigDecimal(searchParam));
            case QUANTITY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get(field.name().toLowerCase()), Long.parseLong(searchParam));
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDateTime searchValue = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return criteriaBuilder.lessThanOrEqualTo(root.get("lastQuantityChangeDate"), searchValue);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), searchValue);
            };
        };
    }
}