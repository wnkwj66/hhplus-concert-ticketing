package com.hhplus.concert_ticketing.app.interfaces.api.controller.queue.dto;

import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueStatus;

public record EnterQueueRes(
        Long concertId,
        Long scheduleId,
        Long userId,
        QueueStatus status) {

    public static EnterQueueRes of(Queue result) {
        return new EnterQueueRes(result.getConcertId(),
                result.getScheduleId(),
                result.getUserId(),
                result.getStatus());
    }
}
