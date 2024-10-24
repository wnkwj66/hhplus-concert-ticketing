package com.hhplus.concert_ticketing.interfaces.api.controller.dto.queue;

import com.hhplus.concert_ticketing.domain.queue.QueueStatus;

public record SelectQueueRes(
        Long position,
        QueueStatus queueStatus
) {
}
