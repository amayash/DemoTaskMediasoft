package com.mediasoft.warehouse.repository;

import com.mediasoft.warehouse.model.Order;
import com.mediasoft.warehouse.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для {@link Order}.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("Select op from OrderProduct op where op.order.id = :orderId and op.product.id = :productId")
    OrderProduct getOrderProduct(@Param("orderId") UUID orderId, @Param("productId") UUID productId);
}

