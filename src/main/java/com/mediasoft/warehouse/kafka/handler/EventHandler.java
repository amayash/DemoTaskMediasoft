package com.mediasoft.warehouse.kafka.handler;

import com.mediasoft.warehouse.kafka.data.KafkaEvent;

/**
 * Интерфейс обработчика событий, обобщенного по типу события T, расширяющего KafkaEvent.
 */
public interface EventHandler<T extends KafkaEvent> {
    /**
     * Проверяет, может ли обработчик обработать указанный источник события.
     *
     * @param eventSource источник события для обработки
     * @return true, если обработчик может обработать событие, в противном случае - false
     */
    boolean canHandle(KafkaEvent eventSource);

    /**
     * Обрабатывает событие и возвращает результат обработки в виде строки.
     *
     * @param eventSource источник события для обработки
     * @return строка с результатом обработки события
     */
    String handleEvent(T eventSource);
}
