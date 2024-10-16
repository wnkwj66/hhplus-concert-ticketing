package com.hhplus.concert_ticketing.interfaces.dto.queue;

import java.time.LocalDateTime;

public record TokenResDto(
        Long tokenId,
        Long userId,
        Long concertId,
        String tokenStauts,
        Integer position,
        LocalDateTime createAt,
        LocalDateTime expiredAt
        ) {
}
