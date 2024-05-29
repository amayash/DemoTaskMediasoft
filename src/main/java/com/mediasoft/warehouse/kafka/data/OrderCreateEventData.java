package com.mediasoft.warehouse.kafka.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mediasoft.warehouse.dto.SaveOrderProductDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Класс, представляющий данные для создания заказа, реализующий интерфейс KafkaEvent.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderCreateEventData implements KafkaEvent {
    /**
     * Идентификатор покупателя.
     */
    @NotNull(message = "Customer id is required")
    @Positive
    private Long customerId;

    /**
     * Адрес доставки.
     */
    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

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
        return Event.CREATE_ORDER;
    }
}
