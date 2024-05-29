package com.mediasoft.warehouse.kafka.data;

/**
 * Перечисление для событий Kafka.
 */
public enum Event {
    CREATE_ORDER,
    UPDATE_ORDER,
    DELETE_ORDER,
    UPDATE_ORDER_STATUS
}
