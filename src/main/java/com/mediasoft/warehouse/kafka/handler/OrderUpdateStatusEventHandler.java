package com.mediasoft.warehouse.kafka.handler;

import com.mediasoft.warehouse.dto.SaveOrderStatusDto;
import com.mediasoft.warehouse.kafka.event.Event;
import com.mediasoft.warehouse.kafka.event.KafkaEvent;
import com.mediasoft.warehouse.kafka.event.OrderUpdateStatusEventData;
import com.mediasoft.warehouse.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Обработчик события "UPDATE_ORDER_STATUS".
 */
@Component
@RequiredArgsConstructor
public class OrderUpdateStatusEventHandler implements EventHandler<OrderUpdateStatusEventData> {
    private final OrderService orderService;

    @Override
    public boolean canHandle(KafkaEvent eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");
        return Event.UPDATE_ORDER_STATUS.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(OrderUpdateStatusEventData eventSource) {
        SaveOrderStatusDto saveOrderStatusDto = new SaveOrderStatusDto();
        saveOrderStatusDto.setStatus(eventSource.getStatus());
        orderService.updateOrderStatus(eventSource.getOrderId(), saveOrderStatusDto);
        return "Order status was updated with ID: " + eventSource.getOrderId();
    }
}
