package com.mediasoft.warehouse.kafka.handler;

import com.mediasoft.warehouse.kafka.data.Event;
import com.mediasoft.warehouse.kafka.data.KafkaEvent;
import com.mediasoft.warehouse.kafka.data.OrderDeleteEventData;
import com.mediasoft.warehouse.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Обработчик события "DELETE_ORDER".
 */
@Component
@RequiredArgsConstructor
public class OrderDeleteEventHandler implements EventHandler<OrderDeleteEventData> {
    private final OrderService orderService;

    @Override
    public boolean canHandle(KafkaEvent eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");
        return Event.DELETE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(OrderDeleteEventData eventSource) {
        orderService.deleteOrder(eventSource.getOrderId(), eventSource.getCustomerId());
        return "Order was deleted with ID: " + eventSource.getOrderId();
    }
}
