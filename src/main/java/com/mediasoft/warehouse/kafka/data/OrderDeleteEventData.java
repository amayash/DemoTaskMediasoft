package com.mediasoft.warehouse.kafka.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Класс, представляющий данные для удаления заказа, реализующий интерфейс KafkaEvent.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderDeleteEventData implements KafkaEvent {
    /**
     * Идентификатор заказа.
     */
    @NotNull(message = "Order id is required")
    private UUID orderId;

    /**
     * Идентификатор покупателя.
     */
    @NotNull(message = "Customer id is required")
    @Positive
    private Long customerId;

    /**
     * Метод для получения типа события.
     *
     * @return тип события, связанный с этим объектом.
     */
    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // Prevents duplication when serializing to JSON (subtype discriminator property)
    public Event getEvent() {
        return Event.DELETE_ORDER;
    }
}
