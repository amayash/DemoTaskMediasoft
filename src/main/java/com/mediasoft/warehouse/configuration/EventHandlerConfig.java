package com.mediasoft.warehouse.configuration;

import com.mediasoft.warehouse.kafka.event.KafkaEvent;
import com.mediasoft.warehouse.kafka.handler.EventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Конфигурационный класс для обработчика событий.
 */
@Configuration
public class EventHandlerConfig {
    /**
     * Конфигурационный класс для обработчика событий.
     */
    @Bean
    <T extends KafkaEvent> Set<EventHandler<T>> eventHandlers(Set<EventHandler<T>> eventHandlers) {
        return new HashSet<>(eventHandlers);
    }
}
