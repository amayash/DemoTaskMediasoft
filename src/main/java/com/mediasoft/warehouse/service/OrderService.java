package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.*;
import com.mediasoft.warehouse.model.Order;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Интерфейс для сервиса управления заказами.
 */
public interface OrderService {
    ViewOrderDto getViewOrderById(UUID orderId, Long customerIdHeader);

    Order createOrder(SaveOrderDto saveOrderDto, Long customerId);

    Order updateOrder(UUID orderId, List<SaveOrderProductDto> saveOrderProductDto, Long customerIdHeader);

    void updateOrderStatus(UUID orderId, SaveOrderStatusDto status);

    void deleteOrder(UUID orderId, Long customerIdHeader);

    CompletableFuture<Map<UUID, List<ViewOrderFromMapDto>>> getOrdersGroupedByProduct();
}
