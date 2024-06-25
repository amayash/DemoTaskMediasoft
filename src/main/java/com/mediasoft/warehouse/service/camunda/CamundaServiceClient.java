package com.mediasoft.warehouse.service.camunda;

import com.mediasoft.warehouse.dto.ConfirmOrderDto;

/**
 * Интерфейс для клиента сервиса камунды.
 */
public interface CamundaServiceClient {
    /**
     * Подтверждает заказ.
     *
     * @param dto данные для подтвекрждения заказа.
     */
    String confirmOrder(ConfirmOrderDto dto);
}
