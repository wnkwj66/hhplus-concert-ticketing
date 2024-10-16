package com.hhplus.concert_ticketing.interfaces.dto.concert;

public record Seat(
        Long seatId,
        Integer seatNumber,
        String status,
        Integer price
) {
}
