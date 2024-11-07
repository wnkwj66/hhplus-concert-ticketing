package com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue;

public record SelectQueueReq(
        Long userId,
        Long concertId,
        Long performanceId
) {
}
