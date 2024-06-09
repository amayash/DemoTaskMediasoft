package com.mediasoft.warehouse.error;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Класс, представляющий детали ошибки.
 * Используется для хранения информации об ошибке.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    /**
     * Класс, в котором произошла ошибка.
     */
    private String className;

    /**
     * Имя исключения, которое было выброшено.
     */
    private String exceptionName;

    /**
     * Сообщение об ошибке.
     */
    private String message;

    /**
     * Время, когда возникла ошибка.
     */
    private LocalDateTime time;
}
