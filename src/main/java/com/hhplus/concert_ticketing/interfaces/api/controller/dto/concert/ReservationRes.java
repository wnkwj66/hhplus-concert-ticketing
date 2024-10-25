package com.hhplus.concert_ticketing.interfaces.api.controller.dto.concert;

import com.hhplus.concert_ticketing.domain.concert.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationRes(
        Long reservationId,
        Long seatId,
        ReservationStatus status,
        LocalDateTime reservedAt,
        LocalDateTime createdAt
) {
}
