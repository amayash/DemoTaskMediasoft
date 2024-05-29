package com.mediasoft.warehouse.kafka.handler;

import com.mediasoft.warehouse.kafka.event.KafkaEvent;
import com.mediasoft.warehouse.model.Order;
import com.mediasoft.warehouse.service.OrderService;
import com.mediasoft.warehouse.kafka.event.Event;
import com.mediasoft.warehouse.kafka.event.OrderUpdateEventData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Обработчик события "UPDATE_ORDER".
 */
@Component
@RequiredArgsConstructor
public class OrderUpdateEventHandler implements EventHandler<OrderUpdateEventData> {
    private final OrderService orderService;

    @Override
    public boolean canHandle(KafkaEvent eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");
        return Event.UPDATE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(OrderUpdateEventData eventSource) {
        Order order = orderService.updateOrder(eventSource.getOrderId(),
                eventSource.getProducts(),
                eventSource.getCustomerId());
        return "Order updated with ID: " + order.getId();
    }
}
