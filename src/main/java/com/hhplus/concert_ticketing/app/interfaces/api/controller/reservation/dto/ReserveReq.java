package com.hhplus.concert_ticketing.app.interfaces.api.controller.reservation.dto;

public record ReserveReq(
        Long concertId,
        Long scheduleId,
        Long seatId
) {
}
