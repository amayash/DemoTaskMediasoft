package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.SaveOrderDto;
import com.mediasoft.warehouse.dto.SaveOrderProductDto;
import com.mediasoft.warehouse.dto.SaveOrderStatusDto;
import com.mediasoft.warehouse.dto.ViewOrderDto;
import com.mediasoft.warehouse.model.Order;

import java.util.List;
import java.util.UUID;

/**
 * Интерфейс для сервиса управления заказами.
 */
public interface OrderService {
    ViewOrderDto getViewOrderById(UUID orderId, Long customerIdHeader);

    Order createOrder(SaveOrderDto saveOrderDto, Long customerId);

    Order updateOrder(UUID orderId, List<SaveOrderProductDto> saveOrderProductDto, Long customerIdHeader);

    void updateOrderStatus(UUID orderId, SaveOrderStatusDto status);

    void deleteOrder(UUID orderId, Long customerIdHeader);
}
