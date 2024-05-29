package com.mediasoft.warehouse.kafka.handler;

import com.mediasoft.warehouse.dto.SaveOrderDto;
import com.mediasoft.warehouse.kafka.data.KafkaEvent;
import com.mediasoft.warehouse.model.Order;
import com.mediasoft.warehouse.service.OrderService;
import com.mediasoft.warehouse.kafka.data.Event;
import com.mediasoft.warehouse.kafka.data.OrderCreateEventData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Обработчик события "CREATE_ORDER".
 */
@Component
@RequiredArgsConstructor
public class OrderCreateEventHandler implements EventHandler<OrderCreateEventData> {
    private final OrderService orderService;

    @Override
    public boolean canHandle(KafkaEvent eventSource) {
        Assert.notNull(eventSource, "EventSource must not be null");
        return Event.CREATE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(OrderCreateEventData eventSource) {
        SaveOrderDto saveOrderDto = new SaveOrderDto();
        saveOrderDto.setDeliveryAddress(eventSource.getDeliveryAddress());
        saveOrderDto.setProducts(eventSource.getProducts());

        Order createdOrder = orderService.createOrder(saveOrderDto, eventSource.getCustomerId());

        return "Order created with ID: " + createdOrder.getId();
    }
}
