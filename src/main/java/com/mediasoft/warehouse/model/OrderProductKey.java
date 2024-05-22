package com.mediasoft.warehouse.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

/**
 * Композитный ключ для сущности {@link OrderProduct}.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderProductKey implements Serializable {
    /**
     * Идентификатор заказа.
     */
    private UUID orderId;

    /**
     * Идентификатор товара.
     */
    private UUID productId;
}
