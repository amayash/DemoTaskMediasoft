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

import java.time.LocalDateTime;
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
        final ErrorDetails errorDetails = new ErrorDetails(e.getStackTrace()[0].getClassName(), e.getClass().getSimpleName(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
    }

    /**
     * Обработка исключений типа MethodArgumentNotValidException.
     *
     * @param e Исключение MethodArgumentNotValidException.
     * @return ResponseEntity с сообщением об ошибке и статусом BAD_REQUEST.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleBindException(MethodArgumentNotValidException e) {
        ErrorDetails errorDetails = new ErrorDetails(
                e.getStackTrace()[0].getClassName(),
                e.getClass().getSimpleName(),
                e.getBindingResult().getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", ")),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDetails);
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
        ErrorDetails errorDetails = new ErrorDetails(
                e.getStackTrace()[0].getClassName(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDetails);
    }
}
