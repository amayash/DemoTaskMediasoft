package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.*;
import com.mediasoft.warehouse.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Rest-API контроллер для заказов.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * Создать заказ.
     *
     * @param saveOrderDto     Информация о новом заказе.
     * @param customerIdHeader Идентификатор покупателя, вызвавшего запрос.
     * @return Идентификатор созданного заказа.
     */
    @PostMapping
    public UUID create(@RequestBody @Valid SaveOrderDto saveOrderDto,
                       @RequestHeader("customerId") Long customerIdHeader) {
        return orderService.createOrder(saveOrderDto, customerIdHeader).getId();
    }

    @PostMapping("/{id}/confirm")
    public void confirm(@PathVariable UUID id) {
    }

    /**
     * Получает информацию о заказах по продуктам.
     *
     * @return Информация о заказах по продуктам.
     */
    @GetMapping
    public CompletableFuture<Map<UUID, List<ViewOrderFromMapDto>>> getAllGroupedByProduct() {
        return orderService.getOrdersGroupedByProduct();
    }

    /**
     * Получает информацию о заказе по его идентификатору.
     *
     * @param id               Идентификатор заказа.
     * @param customerIdHeader Идентификатор покупателя, вызвавшего запрос.
     * @return Информация о заказе.
     */
    @GetMapping("/{id}")
    public ViewOrderDto getById(@PathVariable UUID id, @RequestHeader("customerId") Long customerIdHeader) {
        return orderService.getViewOrderById(id, customerIdHeader);
    }

    /**
     * Обновляет информацию о заказе.
     *
     * @param id                  Идентификатор заказа.
     * @param saveOrderProductDto Информация об обновленном заказе.
     * @param customerIdHeader    Идентификатор покупателя, вызвавшего запрос.
     * @return Идентификатор обновленного заказа.
     */
    @PatchMapping("/{id}")
    public UUID update(@PathVariable UUID id,
                       @RequestBody @Valid List<SaveOrderProductDto> saveOrderProductDto,
                       @RequestHeader("customerId") Long customerIdHeader) {
        return orderService.updateOrder(id, saveOrderProductDto, customerIdHeader).getId();
    }

    /**
     * Обновляет статус заказа.
     *
     * @param id                 Идентификатор заказа.
     * @param saveOrderStatusDto Информация об обновленном статусе заказа.
     */
    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable UUID id, @RequestBody @Valid SaveOrderStatusDto saveOrderStatusDto) {
        orderService.updateOrderStatus(id, saveOrderStatusDto);
    }

    /**
     * Удаляет заказ по его идентификатору.
     *
     * @param id               Идентификатор заказа.
     * @param customerIdHeader Идентификатор покупателя, вызвавшего запрос.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, @RequestHeader("customerId") Long customerIdHeader) {
        orderService.deleteOrder(id, customerIdHeader);
    }
}
