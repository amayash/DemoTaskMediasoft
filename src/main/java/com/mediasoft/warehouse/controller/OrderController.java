package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.SaveOrderDto;
import com.mediasoft.warehouse.dto.SaveOrderProductDto;
import com.mediasoft.warehouse.dto.ViewOrderDto;
import com.mediasoft.warehouse.dto.ViewProductDto;
import com.mediasoft.warehouse.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return new ViewOrderDto(orderService.createOrder(saveOrderDto));
    }

    @GetMapping("/{id}")
    public ViewOrderDto getById(@PathVariable UUID id) {
        return new ViewOrderDto(orderService.getOrderById(id));
    }

    /**
     * Обновить заказ.
     *
     * @param saveOrderProductDto Информация о новом заказе.
     * @return Информация о созданном заказе.
     */
    @PatchMapping("/{id}")
    public ViewOrderDto update(@PathVariable UUID id,
                               @RequestBody @Valid List<SaveOrderProductDto> saveOrderProductDto) {
        return new ViewOrderDto(orderService.updateOrder(id, saveOrderProductDto));
    }
}
