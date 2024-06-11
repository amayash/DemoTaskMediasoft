package com.mediasoft.warehouse.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Класс, представляющий данные для проверки бизнес-ключа заказа, реализующий интерфейс KafkaEvent.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class OrderCheckBusinessKeyEventData implements KafkaEvent {
    @NotBlank(message = "Login is required")
    private String login;

    @NotBlank(message = "Business key is required")
    private String businessKey;

    @NotBlank(message = "CRM is required")
    private String CRM;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Event getEvent() {
        return Event.CHECK_ORDER_BUSINESS_KEY;
    }
}
