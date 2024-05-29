package com.mediasoft.warehouse.kafka.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mediasoft.warehouse.dto.SaveOrderProductDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

/**
 * Класс, представляющий данные для обновления заказа, реализующий интерфейс KafkaEvent.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderUpdateEventData implements KafkaEvent {
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
     * Список продуктов в заказе.
     */
    @NotEmpty(message = "Products are required")
    private List<@Valid SaveOrderProductDto> products;

    /**
     * Метод для получения типа события.
     *
     * @return тип события, связанный с этим объектом.
     */
    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // Prevents duplication when serializing to JSON (subtype discriminator property)
    public Event getEvent() {
        return Event.UPDATE_ORDER;
    }
}
