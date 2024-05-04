package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.SaveOrderDto;
import com.mediasoft.warehouse.dto.SaveOrderProductDto;
import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.error.exception.OrderNotFoundException;
import com.mediasoft.warehouse.model.*;
import com.mediasoft.warehouse.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    /**
     * Получить заказ по идентификатору.
     *
     * @param orderId Идентификатор заказа.
     * @return Заказ с указанным идентификатором.
     * @throws OrderNotFoundException, если заказ не найден.
     */
    @Transactional(readOnly = true)
    public Order getOrderById(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    /**
     * Создать заказ.
     *
     * @param saveOrderDto DTO с информацией о новом заказе.
     * @return Созданный заказ.
     */
    @Transactional
    public Order createOrder(SaveOrderDto saveOrderDto) {
        final Order order = new Order(saveOrderDto);
        final Customer customer = customerService.getCustomerById(1L);
        order.setCustomer(customer);
        Order createdOrder = orderRepository.save(order);
        saveOrderDto.getProducts().stream().
                collect(Collectors.groupingBy(
                        SaveOrderProductDto::getId,
                        Collectors.summingLong(SaveOrderProductDto::getQuantity)
                )).forEach((productId, totalQuantity) ->
                        addProduct(createdOrder, productId, totalQuantity));
        return orderRepository.save(createdOrder);
    }

    @Transactional
    public Order updateOrder(UUID orderId, List<SaveOrderProductDto> saveOrderProductDto) {
        final Order order = getOrderById(orderId);
        saveOrderProductDto.stream().
                collect(Collectors.groupingBy(
                        SaveOrderProductDto::getId,
                        Collectors.summingLong(SaveOrderProductDto::getQuantity)
                )).forEach((productId, totalQuantity) ->
                        addProduct(order, productId, totalQuantity));
        return orderRepository.save(order);
    }

    private void addProduct(Order order, UUID productId, Long quantity) {
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IllegalArgumentException("The order can't be changed anymore");
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
     * Удалить товар по его идентификатору.
     *
     * @param orderId Идентификатор товара, который нужно удалить.
     * @return True, если товар успешно удален, в противном случае - false.
     */
    @Transactional
    public boolean deleteOrder(UUID orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
            return true;
        }
        return false;
    }
}
