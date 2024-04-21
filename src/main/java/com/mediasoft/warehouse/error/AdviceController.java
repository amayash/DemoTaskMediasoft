package com.mediasoft.warehouse.error;

import com.mediasoft.warehouse.error.exception.ProductNotFoundException;
import com.mediasoft.warehouse.error.exception.ValidationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений для Rest-контроллеров.
 */
@ControllerAdvice(annotations = RestController.class)
public class AdviceController {
    /**
     * Обработка исключений ProductNotFoundException, ValidationException
     * jakarta.validation.ValidationException и IllegalArgumentException.
     *
     * @param e Исключение, которое было выброшено.
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST.
     */
    @ExceptionHandler({
            ProductNotFoundException.class,
            ValidationException.class,
            jakarta.validation.ValidationException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<Object> handleException(Throwable e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Обработка исключений типа MethodArgumentNotValidException.
     *
     * @param e Исключение MethodArgumentNotValidException.
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleBindException(MethodArgumentNotValidException e) {
        final ValidationException validationException = new ValidationException(
                e.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toSet()));
        return handleException(validationException);
    }

    /**
     * Обработка общих исключений.
     *
     * @param e Исключение, которое было выброшено.
     * @return ResponseEntity с сообщением об ошибке и статусом INTERNAL_SERVER_ERROR.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnknownException(Throwable e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
