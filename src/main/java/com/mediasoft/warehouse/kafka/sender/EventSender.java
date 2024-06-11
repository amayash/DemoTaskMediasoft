package com.mediasoft.warehouse.kafka.sender;

import com.mediasoft.warehouse.dto.ClaimDto;
import com.mediasoft.warehouse.kafka.event.Event;

public interface EventSender {
    boolean canSend(final Event orderEventType);
    void sendEvent(final ClaimDto data, final String processId);
}

