package com.hhplus.concert_ticketing.interfaces.api.controller.dto.queue;

public record CreateQueueReq(
        Long userId,
        Long concertId,
        Long performanceId) {
}
