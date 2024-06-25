package com.mediasoft.warehouse.kafka.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Интерфейс для событий Kafka.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "event"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderCreateEventData.class, name = "CREATE_ORDER"),
        @JsonSubTypes.Type(value = OrderUpdateEventData.class, name = "UPDATE_ORDER"),
        @JsonSubTypes.Type(value = OrderDeleteEventData.class, name = "DELETE_ORDER"),
        @JsonSubTypes.Type(value = OrderUpdateStatusEventData.class, name = "UPDATE_ORDER_STATUS")
})
public interface KafkaEvent {
    /**
     * Метод для получения типа события.
     *
     * @return тип события.
     */
    Event getEvent();
}