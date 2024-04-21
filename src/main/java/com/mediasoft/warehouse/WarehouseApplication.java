package com.mediasoft.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Основной класс приложения.
 */
@SpringBootApplication
@EnableJpaAuditing
public class WarehouseApplication {
    /**
     * Метод для запуска приложения.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        SpringApplication.run(WarehouseApplication.class, args);
    }
}
