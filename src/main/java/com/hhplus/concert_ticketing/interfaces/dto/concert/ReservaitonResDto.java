package com.hhplus.concert_ticketing.interfaces.dto.concert;

import java.time.LocalDateTime;

public record ReservaitonResDto(
        Long reservationId,
        Long userId,
        Long concertId,
        String reservationStatus,
        LocalDateTime reservationAt,
        LocalDateTime performanceAt,
        Integer seatNo
) {
}
