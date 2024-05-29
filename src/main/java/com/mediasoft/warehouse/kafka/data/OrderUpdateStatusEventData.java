package com.mediasoft.warehouse.kafka.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mediasoft.warehouse.model.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Класс, представляющий данные для обновления статуса заказа, реализующий интерфейс KafkaEvent.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderUpdateStatusEventData implements KafkaEvent {
    /**
     * Идентификатор заказа.
     */
    @NotNull(message = "Order id is required")
    private UUID orderId;

    /**
     * Статус заказа.
     */
    @NotNull(message = "Order status is required")
    private OrderStatus status;

    /**
     * Метод для получения типа события.
     *
     * @return тип события, связанный с этим объектом.
     */
    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // Prevents duplication when serializing to JSON (subtype discriminator property)
    public Event getEvent() {
        return Event.UPDATE_ORDER_STATUS;
    }
}
