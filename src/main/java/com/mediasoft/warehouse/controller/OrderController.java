package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.SaveOrderDto;
import com.mediasoft.warehouse.dto.SaveOrderProductDto;
import com.mediasoft.warehouse.dto.SaveOrderStatusDto;
import com.mediasoft.warehouse.dto.ViewOrderDto;
import com.mediasoft.warehouse.error.exception.ForbiddenException;
import com.mediasoft.warehouse.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;
import java.util.UUID;

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
     * @param saveOrderDto Информация о новом заказе.
     * @return Информация о созданном заказе.
     */
    @PostMapping
    public ViewOrderDto create(@RequestBody @Valid SaveOrderDto saveOrderDto) {
        return new ViewOrderDto(orderService.createOrder(saveOrderDto, 1L));
    }

    @PostMapping("/{id}/confirm")
    public ViewOrderDto confirm(@PathVariable UUID id) {
        return null;
    }

    /**
     * Получает информацию о заказе по его идентификатору.
     *
     * @param id Идентификатор заказа.
     * @return Информация о заказе.
     * @throws ForbiddenException если пользователь не имеет доступа к заказу.
     */
    @GetMapping("/{id}")
    public ViewOrderDto getById(@PathVariable UUID id) {
        if (new Random().nextBoolean()) {
            throw new ForbiddenException("You don't have access to other users' orders");
        }
        return new ViewOrderDto(orderService.getOrderById(id));
    }

    /**
     * Обновляет информацию о заказе.
     *
     * @param id                  Идентификатор заказа.
     * @param saveOrderProductDto Информация об обновленном заказе.
     * @return Информация об обновленном заказе.
     * @throws ForbiddenException если пользователь не имеет доступа к заказу.
     */
    @PatchMapping("/{id}")
    public ViewOrderDto update(@PathVariable UUID id,
                               @RequestBody @Valid List<SaveOrderProductDto> saveOrderProductDto) {
        if (new Random().nextBoolean()) {
            throw new ForbiddenException("You don't have access to other users' orders");
        }
        return new ViewOrderDto(orderService.updateOrder(id, saveOrderProductDto));
    }

    /**
     * Обновляет статус заказа.
     *
     * @param id                 Идентификатор заказа.
     * @param saveOrderStatusDto Информация об обновленном статусе заказа.
     * @return Информация об обновленном заказе.
     */
    @PatchMapping("/{id}/status")
    public ViewOrderDto updateStatus(@PathVariable UUID id, @RequestBody @Valid SaveOrderStatusDto saveOrderStatusDto) {
        return new ViewOrderDto(orderService.updateOrderStatus(id, saveOrderStatusDto));
    }

    /**
     * Удаляет заказ по его идентификатору.
     *
     * @param id Идентификатор заказа.
     * @return true, если заказ успешно удален.
     * @throws ForbiddenException если пользователь не имеет доступа к заказу.
     */
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable UUID id) {
        if (new Random().nextBoolean()) {
            throw new ForbiddenException("You don't have access to other users' orders");
        }
        orderService.deleteOrder(id);
        return true;
    }
}
