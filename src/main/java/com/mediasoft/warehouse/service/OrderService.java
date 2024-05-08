package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.SaveOrderDto;
import com.mediasoft.warehouse.dto.SaveOrderProductDto;
import com.mediasoft.warehouse.dto.SaveOrderStatusDto;
import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.error.exception.ForbiddenException;
import com.mediasoft.warehouse.error.exception.IncorrectOrderStatusException;
import com.mediasoft.warehouse.error.exception.OrderNotFoundException;
import com.mediasoft.warehouse.model.*;
import com.mediasoft.warehouse.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для управления заказами.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    /**
     * Получить заказ по идентификатору.
     *
     * @param orderId          Идентификатор заказа.
     * @param customerIdHeader Идентификатор покупателя, вызвавшего запрос.
     * @return Заказ с указанным идентификатором.
     * @throws OrderNotFoundException, если заказ не найден.
     */
    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId, Long customerIdHeader) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        if (customerIdHeader != null)
            checkCustomerIdMatchers(order.getCustomer().getId(), customerIdHeader);
        return order;
    }

    /**
     * Создать новый заказ.
     *
     * @param saveOrderDto Данные о новом заказе.
     * @param customerId   Идентификатор покупателя.
     * @return Созданный заказ.
     */
    @Transactional
    public Order createOrder(SaveOrderDto saveOrderDto, Long customerId) {
        final Order order = new Order(saveOrderDto);
        final Customer customer = customerService.getCustomerById(customerId);
        order.setCustomer(customer);
        Order createdOrder = orderRepository.save(order);
        saveOrderDto.getProducts().stream()
                .collect(Collectors.groupingBy(
                        SaveOrderProductDto::getId,
                        Collectors.summingLong(SaveOrderProductDto::getQuantity)))
                .forEach((productId, totalQuantity) -> addProductInOrder(createdOrder, productId, totalQuantity));
        return orderRepository.save(createdOrder);
    }

    /**
     * Обновить информацию о заказе.
     *
     * @param orderId             Идентификатор заказа.
     * @param saveOrderProductDto Информация о товарах в заказе.
     * @param customerIdHeader    Идентификатор покупателя, вызвавшего запрос.
     * @return Обновленный заказ.
     */
    @Transactional
    public Order updateOrder(UUID orderId, List<SaveOrderProductDto> saveOrderProductDto, Long customerIdHeader) {
        final Order order = getOrderById(orderId, customerIdHeader);
        checkCustomerIdMatchers(order.getCustomer().getId(), customerIdHeader);
        saveOrderProductDto.stream().
                collect(Collectors.groupingBy(
                        SaveOrderProductDto::getId,
                        Collectors.summingLong(SaveOrderProductDto::getQuantity)
                )).forEach((productId, totalQuantity) ->
                        addProductInOrder(order, productId, totalQuantity));
        return orderRepository.save(order);
    }

    /**
     * Добавляет товар в заказ.
     *
     * @param order     Заказ, в который добавляется товар.
     * @param productId Идентификатор товара, который добавляется в заказ.
     * @param quantity  Количество товара, которое добавляется в заказ.
     * @throws IncorrectOrderStatusException, если статус заказа не является CREATED.
     * @throws IllegalArgumentException,      если товар недоступен или если его количество не соответствует требуемому.
     */
    private void addProductInOrder(Order order, UUID productId, Long quantity) {
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IncorrectOrderStatusException(order.getStatus().name());
        }
        final Product currentProduct = productService.getProductById(productId);
        if (!currentProduct.getIsAvailable()) {
            throw new IllegalArgumentException("Products not available");
        }
        final Long currentProductCapacity = currentProduct.getQuantity();
        if (currentProductCapacity < quantity) {
            throw new IllegalArgumentException(String.format("No more products. Capacity: %1$s. Quantity: %2$s",
                    currentProductCapacity, quantity));
        }
        final OrderProduct orderProduct = orderRepository.getOrderProduct(order.getId(), productId);
        if (orderProduct == null) {
            order.addProduct(new OrderProduct(order, currentProduct, quantity));
        } else {
            order.getProducts().stream()
                    .filter(x -> x == orderProduct)
                    .findFirst()
                    .ifPresent(x -> {
                        x.setQuantity(x.getQuantity() + quantity);
                        x.setFrozenPrice(currentProduct.getPrice());
                    });
        }
        currentProduct.setQuantity(currentProductCapacity - quantity);
        productService.updateProduct(productId, new SaveProductDto(currentProduct));
    }

    /**
     * Изменить статус заказа.
     *
     * @param orderId Идентификатор заказа.
     * @param status  Новый статус заказа.
     * @return Обновленный заказ с новым статусом.
     */
    @Transactional
    public Order updateOrderStatus(UUID orderId, SaveOrderStatusDto status) {
        Order order = getOrderById(orderId, null);
        order.setStatus(status.getStatus());
        return orderRepository.save(order);
    }

    /**
     * Удалить заказ по его идентификатору.
     *
     * @param orderId          Идентификатор заказа, который нужно удалить.
     * @param customerIdHeader Идентификатор покупателя, вызвавшего запрос.
     * @throws IncorrectOrderStatusException, если статус заказа не является CREATED.
     */
    @Transactional
    public void deleteOrder(UUID orderId, Long customerIdHeader) {
        Order order = getOrderById(orderId, customerIdHeader);
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IncorrectOrderStatusException(order.getStatus().name());
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.getProducts().forEach(orderProduct -> {
            Product product = productService.getProductById(orderProduct.getProduct().getId());
            product.setQuantity(product.getQuantity() + orderProduct.getQuantity());
            productService.updateProduct(product.getId(), new SaveProductDto(product));
        });
        orderRepository.save(order);
    }

    /**
     * Проверить соответствие идентификатора покупателя идентификатору покупателя, вызвавшего запрос.
     *
     * @param customerId       Идентификатор покупателя из заказа.
     * @param customerIdHeader Идентификатор покупателя, вызвавшего запрос.
     * @throws ForbiddenException, если идентификаторы не совпали.
     */
    private void checkCustomerIdMatchers(Long customerId, Long customerIdHeader) {
        if (!customerId.equals(customerIdHeader)) {
            throw new ForbiddenException("You don't have access to other users' orders");
        }
    }
}
