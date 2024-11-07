package com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.concert;

public record ReservationReq(
        Long performanceId,
        Long seatId
) {
}
