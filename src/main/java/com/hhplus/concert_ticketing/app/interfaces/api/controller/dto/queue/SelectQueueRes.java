package com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue;

import com.hhplus.concert_ticketing.app.domain.queue.QueueStatus;

public record SelectQueueRes(
        Long position,
        QueueStatus queueStatus
) {
}
