package com.mediasoft.warehouse.kafka.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mediasoft.warehouse.dto.ClaimDto;
import com.mediasoft.warehouse.kafka.Producer;
import com.mediasoft.warehouse.kafka.event.Event;
import com.mediasoft.warehouse.kafka.event.OrderCheckBusinessKeyEventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCheckBusinessKeyEventSender implements EventSender {
    private final Producer producer;

    @Override
    public boolean canSend(final Event orderEventType) {
        Assert.notNull(orderEventType, "Event must not be null");
        return Event.CREATE_ORDER.equals(orderEventType);
    }

    @Override
    public void sendEvent(final ClaimDto data, final String processId) {
        final OrderCheckBusinessKeyEventData orderCheckBusinessKeyEventData = OrderCheckBusinessKeyEventData
                .builder()
                .login(data.getLogin())
                .businessKey(data.getBusinessKey())
                .CRM(data.getCRM())
                .build();
        try {
            producer.sendEvent(Producer.PRODUCER_TOPIC, processId, orderCheckBusinessKeyEventData);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
