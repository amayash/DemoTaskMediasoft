package com.mediasoft.warehouse.util.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Компонент валидатора для валидации объектов.
 */
@Component
public class ValidatorUtil {

    private final Validator validator;

    /**
     * Конструктор компонента валидатора, инициализирует встроенный валидатор.
     */
    public ValidatorUtil() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Выполняет валидацию объекта и выбрасывает исключение ValidationException при ошибках валидации.
     *
     * @param object Объект для валидации.
     * @param <T>    Тип объекта.
     * @throws ValidationException при ошибке валидации.
     */
    public <T> void validate(T object) {
        final Set<ConstraintViolation<T>> errors = validator.validate(object);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet()));
        }
    }
}
