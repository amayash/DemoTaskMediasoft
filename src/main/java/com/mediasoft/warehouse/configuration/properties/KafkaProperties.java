package com.mediasoft.warehouse.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настроек Kafka.
 */
@Configuration
@ConfigurationProperties(prefix = "app.kafka")
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled")
@Getter
@Setter
public class KafkaProperties {
    /**
     * Адреса серверов Kafka, к которым будет подключаться потребитель.
     */
    private String bootstrapAddress;

    /**
     * Идентификатор группы потребителей.
     */
    private String groupId;
}
