package com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.concert;

import com.hhplus.concert_ticketing.app.domain.concert.ConcertStatus;

import java.time.LocalDateTime;

public record SelectPerformanceRes(
        Long performanceId,
        ConcertStatus status,
        LocalDateTime performanceAt,
        Integer availableSeat,
        Integer totalSeat
) {
}