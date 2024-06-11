package com.mediasoft.warehouse.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediasoft.warehouse.kafka.event.KafkaEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.CompletableFuture;

/**
 * Сервис для отправления сообщений в Kafka.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled")
public class Producer {
    private final KafkaTemplate<String, byte[]> kafkaTemplateByteArray;
    public static final String PRODUCER_TOPIC = "producer_topic";

    public void sendEvent(final String topic, final String key, final KafkaEvent event) throws JsonProcessingException {
        Assert.hasText(topic, "topic must not be blank");
        Assert.hasText(key, "key must not be blank");
        Assert.notNull(event, "KafkaEvent must not be null");

        final ObjectMapper objectMapper = new ObjectMapper();
        final byte[] data = objectMapper.writeValueAsBytes(event);

        CompletableFuture<SendResult<String, byte[]>> future = kafkaTemplateByteArray.send(topic, key, data);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Kafka send complete: {}", result);
            } else {
                log.error("Kafka fail send", ex);
            }
        });
    }
}
