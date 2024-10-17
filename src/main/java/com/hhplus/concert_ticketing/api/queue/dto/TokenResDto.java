package com.hhplus.concert_ticketing.api.queue.dto;

import java.time.LocalDateTime;

public record TokenResDto(
        Long id,
        Long userId,
        Long concertId,
        String tokenUUID,
        String tokenStauts,
        Integer position,
        LocalDateTime createAt,
        LocalDateTime expiredAt
        ) {
}
