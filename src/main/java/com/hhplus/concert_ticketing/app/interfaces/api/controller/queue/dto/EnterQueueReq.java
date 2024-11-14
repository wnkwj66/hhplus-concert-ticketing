package com.hhplus.concert_ticketing.app.interfaces.api.controller.queue.dto;

public record EnterQueueReq(
        Long userId,
        Long concertId,
        Long scheduleId) {
}
