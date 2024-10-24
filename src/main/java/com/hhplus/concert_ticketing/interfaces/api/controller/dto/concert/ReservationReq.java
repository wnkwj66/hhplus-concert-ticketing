package com.hhplus.concert_ticketing.interfaces.api.controller.dto.concert;

public record ReservationReq(
        Long performanceId,
        Long seatId
) {
}
