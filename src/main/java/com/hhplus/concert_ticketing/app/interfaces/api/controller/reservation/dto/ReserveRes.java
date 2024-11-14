package com.hhplus.concert_ticketing.app.interfaces.api.controller.reservation.dto;

import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.domain.reservation.ReservationStatus;

import java.time.LocalDateTime;

public record ReserveRes(
        Long reservationId,
        Long seatId,
        ReservationStatus status,
        LocalDateTime reservedAt,
        LocalDateTime createdAt
) {
    public static ReserveRes of(Reservation result) {
        return new ReserveRes(result.getId(),
                result.getSeatId(),
                result.getStatus(),
                result.getReservationAt(),
                result.getCreateAt());
    }
}
