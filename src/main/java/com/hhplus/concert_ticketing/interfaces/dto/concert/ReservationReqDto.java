package com.hhplus.concert_ticketing.interfaces.dto.concert;

import java.time.LocalDateTime;

public record ReservationReqDto(
        Long userId,
        Long concertId,
        Long performanceId,
        Integer seatNumber,
        LocalDateTime performanceAt
) {
}
