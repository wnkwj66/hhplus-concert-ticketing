package com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.concert;

import com.hhplus.concert_ticketing.app.domain.concert.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationRes(
        Long reservationId,
        Long seatId,
        ReservationStatus status,
        LocalDateTime reservedAt,
        LocalDateTime createdAt
) {
}
