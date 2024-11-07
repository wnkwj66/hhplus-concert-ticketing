package com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.concert;

import com.hhplus.concert_ticketing.app.domain.concert.SeatStatus;

import java.time.LocalDateTime;

public record SelectSeatRes(
        Long id,
        Long performanceId,
        Integer price,
        SeatStatus status,
        LocalDateTime expirdeAt
) {
}
