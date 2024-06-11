package com.mediasoft.warehouse.service;

import com.mediasoft.warehouse.dto.SaveOrderDto;
import com.mediasoft.warehouse.dto.SaveOrderProductDto;
import com.mediasoft.warehouse.dto.SaveOrderStatusDto;
import com.mediasoft.warehouse.dto.SaveProductDto;
import com.mediasoft.warehouse.dto.ViewCustomerFromOrderDto;
import com.mediasoft.warehouse.dto.ViewOrderDto;
import com.mediasoft.warehouse.dto.ViewOrderFromMapDto;
import com.mediasoft.warehouse.dto.ViewOrderProductDto;
import com.mediasoft.warehouse.error.exception.ForbiddenException;
import com.mediasoft.warehouse.error.exception.IncorrectOrderStatusException;
import com.mediasoft.warehouse.error.exception.NotEnoughProductException;
import com.mediasoft.warehouse.error.exception.OrderNotFoundException;
import com.mediasoft.warehouse.error.exception.ProductNotAvailableException;
import com.mediasoft.warehouse.model.Order;
import com.mediasoft.warehouse.model.OrderProduct;
import com.mediasoft.warehouse.model.OrderProductKey;
import com.mediasoft.warehouse.model.Product;
import com.mediasoft.warehouse.model.enums.OrderStatus;
import com.mediasoft.warehouse.repository.OrderRepository;
import com.mediasoft.warehouse.service.account.AccountServiceClient;
import com.mediasoft.warehouse.service.crm.CrmServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис для управления заказами.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final AccountServiceClient accountServiceClient;
    private final CrmServiceClient crmServiceClient;
    private final RuntimeService runtimeService;
    private static final String PROCESS_KEY = "DemoCamundaProcessKey";

    /**
     * Получить заказ по идентификатору.
     *
     * @param orderId          Идентификатор заказа.
     * @param customerIdHeader Идентификатор покупателя, вызвавшего запрос.
     * @return Заказ с указанным идентификатором.
     * @throws OrderNotFoundException, если заказ не найден.
     */
    public ViewOrderDto getViewOrderById(UUID orderId, Long customerIdHeader) {
        Order order = orderRepository.findByIdWithoutProducts(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        checkCustomerIdMatchers(order.getCustomer().getId(), customerIdHeader);

        List<ViewOrderProductDto> products = orderRepository.findViewOrderProductsByOrderId(orderId);
        BigDecimal totalPrice = products.stream()
                .map(product -> product.getFrozenPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new ViewOrderDto(orderId, products, totalPrice);
    }

    /**
     * Получить заказ по идентификатору.
     *
     * @param orderId          Идентификатор заказа.
     * @param customerIdHeader Идентификатор покупателя, вызвавшего запрос.
     * @return Заказ с указанным идентификатором.
     * @throws OrderNotFoundException, если заказ не найден.
     */
    private Order getOrderById(UUID orderId, Long customerIdHeader) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        if (customerIdHeader != null) {
            checkCustomerIdMatchers(order.getCustomer().getId(), customerIdHeader);
        }
        return order;
    }

    /**
     * Создать новый заказ.
     *
     * @param saveOrderDto Данные о новом заказе.
     * @param customerId   Идентификатор покупателя.
     * @return Созданный заказ.
     * @throws ProductNotAvailableException, если какой-то товар недоступен.
     * @throws NotEnoughProductException,    если какого-то товара не хватает.
     */
    @Transactional
    public Order createOrder(SaveOrderDto saveOrderDto, Long customerId) {
        final Order order = new Order();
        order.setStatus(OrderStatus.CREATED);
        order.setDeliveryAddress(saveOrderDto.getDeliveryAddress());
        order.setProducts(new ArrayList<>());
        order.setCustomer(customerService.getCustomerById(customerId));

        Map<UUID, Long> productIdsAndQuantities = saveOrderDto.getProducts().stream()
                .collect(Collectors.groupingBy(
                        SaveOrderProductDto::getId,
                        Collectors.summingLong(SaveOrderProductDto::getQuantity))
                );
        Map<UUID, Product> productsMap = productService.getProductsByIds(productIdsAndQuantities.keySet()).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        productIdsAndQuantities.forEach((productId, quantity) -> {
            Product product = productsMap.get(productId);
            if (!product.getIsAvailable()) {
                throw new ProductNotAvailableException(productId);
            }
            final Long currentProductCapacity = product.getQuantity();
            if (currentProductCapacity < quantity) {
                throw new NotEnoughProductException(productId, currentProductCapacity, quantity);
            }
            final OrderProduct newOrderProduct = new OrderProduct(new OrderProductKey(order.getId(), productId), order, product, quantity, product.getPrice());
            order.getProducts().add(newOrderProduct);
            product.setQuantity(currentProductCapacity - quantity);
        });
        return orderRepository.save(order);
    }

    @Transactional
    public String confirmOrder(UUID orderId) {
        updateOrderStatus(orderId, new SaveOrderStatusDto(OrderStatus.PROCESSING));
        Order order = getOrderById(orderId, null);
        List<String> userLogin = List.of(order.getCustomer().getLogin());

        return runtimeService
                .createProcessInstanceByKey(PROCESS_KEY)
                .businessKey(UUID.randomUUID().toString())
                .setVariable("orderDeliveryAddress", order.getDeliveryAddress())
                .setVariable("customerCRM", crmServiceClient.getCrms(userLogin))
                .setVariable("customerAccountNumber", accountServiceClient.getAccounts(userLogin))
                .setVariable("orderPrice", order.getProducts()
                        .stream()
                        .map(product -> product.getFrozenPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .setVariable("customerLogin", userLogin.getFirst())
                .execute()
                .getBusinessKey();
    }

    public void checkOrderBusinessKey(String businessKey, String login, String CRM) {
        boolean incorrectOrderBusinessKey = Math.random() % 3 == 0;
        runtimeService.createMessageCorrelation("compliance")
                .processInstanceBusinessKey(businessKey)
                .setVariable("incorrectOrderBusinessKey", incorrectOrderBusinessKey)
                .correlate();
    }

    /**
     * Обновить информацию о заказе.
     *
     * @param orderId             Идентификатор заказа.
     * @param saveOrderProductDto Информация о товарах в заказе.
     * @param customerIdHeader    Идентификатор покупателя, вызвавшего запрос.
     * @return Обновленный заказ.
     * @throws IncorrectOrderStatusException, если статус заказа не является CREATED.
     * @throws ProductNotAvailableException,  если какой-то товар недоступен.
     * @throws NotEnoughProductException,     если какого-то товара не хватает.
     */
    @Transactional
    public Order updateOrder(UUID orderId, List<SaveOrderProductDto> saveOrderProductDto, Long customerIdHeader) {
        final Order order = getOrderById(orderId, customerIdHeader);
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new IncorrectOrderStatusException(order.getStatus().name());
        }

        Map<UUID, OrderProduct> orderProductsMap = order.getProducts().stream()
                .collect(Collectors.toMap(op -> op.getProduct().getId(), Function.identity()));
        Map<UUID, Long> productIdsAndQuantities = saveOrderProductDto.stream()
                .collect(Collectors.groupingBy(
                        SaveOrderProductDto::getId,
                        Collectors.summingLong(SaveOrderProductDto::getQuantity)
                ));
        Map<UUID, Product> productsMap = productService.getProductsByIds(productIdsAndQuantities.keySet()).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        productIdsAndQuantities.forEach((productId, quantity) -> {
            final Product product = productsMap.get(productId);
            final OrderProduct orderProduct = orderProductsMap.get(productId);
            if (!product.getIsAvailable()) {
                throw new ProductNotAvailableException(productId);
            }
            Long currentProductCapacity = product.getQuantity();
            if (currentProductCapacity < quantity) {
                throw new NotEnoughProductException(productId, currentProductCapacity, quantity);
            }
            OrderProduct updatedOrderProduct = orderProduct;
            if (orderProduct == null) {
                updatedOrderProduct = new OrderProduct(new OrderProductKey(order.getId(), productId),
                        order, product, quantity, product.getPrice());
                order.getProducts().add(updatedOrderProduct);
            } else {
                updatedOrderProduct.setQuantity(updatedOrderProduct.getQuantity() + quantity);
                updatedOrderProduct.setFrozenPrice(product.getPrice());
            }
            product.setQuantity(currentProductCapacity - quantity);
        });
        return orderRepository.save(order);
    }

    /**
     * Изменить статус заказа.
     *
     * @param orderId Идентификатор заказа.
     * @param status  Новый статус заказа.
     */
    @Transactional
    public void updateOrderStatus(UUID orderId, SaveOrderStatusDto status) {
        Order order = getOrderById(orderId, null);
        order.setStatus(status.getStatus());
        orderRepository.save(order);
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
     * Получает данные о заказах по товарам.
     *
     * @return Объект {@link Map}, содержащий информацию о заказах по ID товаров.
     */
    @Transactional
    public Map<UUID, List<ViewOrderFromMapDto>> getOrdersGroupedByProduct() throws ExecutionException, InterruptedException {
        // Получаем все заказы в указанных статусах
        List<Order> orders = orderRepository.findByStatusIn(List.of(OrderStatus.CREATED, OrderStatus.CONFIRMED));

        // Собираем логины покупателей
        List<String> logins = orders.stream()
                .map(order -> order.getCustomer().getLogin())
                .distinct()
                .collect(Collectors.toList());

        // Делаем асинхронные запросы к внешним сервисам
        CompletableFuture<Map<String, String>> accountsFuture = accountServiceClient.getAccounts(logins);
        CompletableFuture<Map<String, String>> crmsFuture = crmServiceClient.getCrms(logins);

        return CompletableFuture.allOf(accountsFuture, crmsFuture)
                .thenApply(voidResult -> orders.stream()
                        .flatMap(order -> order.getProducts().stream()
                                .map(productId -> new AbstractMap.SimpleEntry<>(productId, order)))
                        .collect(Collectors.groupingBy(
                                entry -> entry.getKey().getProduct().getId(),
                                Collectors.mapping(entry -> {
                                    Order order = entry.getValue();
                                    ViewCustomerFromOrderDto customerDto = new ViewCustomerFromOrderDto(
                                            order.getCustomer().getId(),
                                            accountsFuture.join().get(order.getCustomer().getLogin()),
                                            order.getCustomer().getEmail(),
                                            crmsFuture.join().get(order.getCustomer().getLogin())
                                    );
                                    return new ViewOrderFromMapDto(
                                            order.getId(),
                                            customerDto,
                                            order.getStatus(),
                                            order.getDeliveryAddress(),
                                            entry.getKey().getQuantity()
                                    );
                                }, Collectors.toList())
                        ))).get();
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
