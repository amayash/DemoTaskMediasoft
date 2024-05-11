package com.mediasoft.warehouse.controller;

import com.mediasoft.warehouse.dto.SaveOrderDto;
import com.mediasoft.warehouse.dto.SaveOrderProductDto;
import com.mediasoft.warehouse.dto.SaveOrderStatusDto;
import com.mediasoft.warehouse.dto.ViewOrderDto;
import com.mediasoft.warehouse.service.OrderServiceImpl;
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
    private final OrderServiceImpl orderServiceImpl;

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
        return orderServiceImpl.createOrder(saveOrderDto, customerIdHeader).getId();
    }

    @PostMapping("/{id}/confirm")
    public void confirm(@PathVariable UUID id) {
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
        return orderServiceImpl.getViewOrderById(id, customerIdHeader);
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
        return orderServiceImpl.updateOrder(id, saveOrderProductDto, customerIdHeader).getId();
    }

    /**
     * Обновляет статус заказа.
     *
     * @param id                 Идентификатор заказа.
     * @param saveOrderStatusDto Информация об обновленном статусе заказа.
     */
    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable UUID id, @RequestBody @Valid SaveOrderStatusDto saveOrderStatusDto) {
        orderServiceImpl.updateOrderStatus(id, saveOrderStatusDto);
    }

    /**
     * Удаляет заказ по его идентификатору.
     *
     * @param id               Идентификатор заказа.
     * @param customerIdHeader Идентификатор покупателя, вызвавшего запрос.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id, @RequestHeader("customerId") Long customerIdHeader) {
        orderServiceImpl.deleteOrder(id, customerIdHeader);
    }
}
