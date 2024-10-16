package com.hhplus.concert_ticketing.interfaces.dto.concert;

import java.time.LocalDateTime;
import java.util.List;

public record SeatResDto(
        Long concertId,
        String concertName,
        Long performanceId,
        LocalDateTime performanceAt,
        List<Seat> seats
) {
}
