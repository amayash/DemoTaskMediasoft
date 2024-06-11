package com.mediasoft.warehouse.kafka.handler;

import com.mediasoft.warehouse.kafka.event.Event;
import com.mediasoft.warehouse.kafka.event.KafkaEvent;
import com.mediasoft.warehouse.kafka.event.OrderCheckBusinessKeyEventData;
import com.mediasoft.warehouse.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Обработчик события "CHECK_ORDER_BUSINESS_KEY".
 */
@Component
@RequiredArgsConstructor
public class OrderCheckBusinessKeyEventHandler implements EventHandler<OrderCheckBusinessKeyEventData> {
    private final OrderService orderService;

    @Override
    public boolean canHandle(KafkaEvent eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");
        return Event.CHECK_ORDER_BUSINESS_KEY.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(OrderCheckBusinessKeyEventData eventSource) {
        orderService.checkOrderBusinessKey(eventSource.getBusinessKey(), eventSource.getLogin(), eventSource.getCRM());
        return eventSource.getBusinessKey();
    }
}
