package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.dto.ViewOrderProductDto;
import com.mediasoft.warehouse.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для {@link Order}.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    /**
     * Получить заказ по идентификатору и загрузить связанные товары.
     *
     * @param uuid Идентификатор заказа.
     * @return Заказ с подгруженными товарами.
     */
    @Override
    @EntityGraph(attributePaths = "products.product")
    Optional<Order> findById(UUID uuid);

    /**
     * Получить заказ по идентификатору.
     *
     * @param uuid Идентификатор заказа.
     * @return Заказ.
     */
    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.id = :uuid")
    @EntityGraph(attributePaths = "customer")
    Order findByIdWithoutProducts(UUID uuid);

    /**
     * Получить информацию о том, существует ли заказ с указанным идентификатором.
     *
     * @param uuid Идентификатор для проверки.
     * @return Флаг, указывающий существует ли заказ с указанным идентификатором.
     */
    boolean existsById(UUID uuid);

    /**
     * Получить товары заказа.
     *
     * @param orderId Идентификатор заказа.
     * @return Товары заказа.
     */
    @Query("SELECT new com.mediasoft.warehouse.dto.ViewOrderProductDto(op.product.id, op.product.name, op.quantity, op.frozenPrice) " +
            "FROM OrderProduct op " +
            "WHERE op.order.id = :orderId")
    List<ViewOrderProductDto> findViewOrderProductsByOrderId(UUID orderId);
}