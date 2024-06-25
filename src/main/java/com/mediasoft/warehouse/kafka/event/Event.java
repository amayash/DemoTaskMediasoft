package com.mediasoft.warehouse.kafka.event;

/**
 * Перечисление для событий Kafka.
 */
public enum Event {
    CREATE_ORDER,
    UPDATE_ORDER,
    DELETE_ORDER,
    UPDATE_ORDER_STATUS,
    CHECK_ORDER_BUSINESS_KEY
}
