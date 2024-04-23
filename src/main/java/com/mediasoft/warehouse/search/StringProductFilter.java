package com.mediasoft.warehouse.search;

import com.mediasoft.warehouse.error.exception.CompareWithStringException;
import com.mediasoft.warehouse.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

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
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), "%" + searchParam + "%");
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), "%" + searchParam + "%");
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
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), UUID.fromString(searchParam));
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get(field.name().toLowerCase()), searchParam);
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchDate = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME).toLocalDate();
                return criteriaBuilder.equal(root.get("lastQuantityChangeDate").as(LocalDate.class), searchDate);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                return criteriaBuilder.equal(root.get("createdDate"), searchValue);
            };
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
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), searchParam + "%");
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), searchParam + "%");
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDateTime searchValue = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("lastQuantityChangeDate"), searchValue);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), searchValue);
            };
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
            case ID -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()).as(String.class), "%" + searchParam);
            case NAME, ARTICLE, DESCRIPTION, CATEGORY -> (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(field.name().toLowerCase()), "%" + searchParam);
            case LAST_QUANTITY_CHANGE_DATE -> (root, query, criteriaBuilder) -> {
                LocalDateTime searchValue = LocalDateTime.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return criteriaBuilder.lessThanOrEqualTo(root.get("lastQuantityChangeDate"), searchValue);
            };
            case CREATED_DATE -> (root, query, criteriaBuilder) -> {
                LocalDate searchValue = LocalDate.parse(searchParam, DateTimeFormatter.ISO_LOCAL_DATE);
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), searchValue);
            };
            default -> throw new CompareWithStringException(field.name());
        };
    }
}