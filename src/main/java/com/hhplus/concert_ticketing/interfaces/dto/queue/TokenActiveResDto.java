package com.hhplus.concert_ticketing.interfaces.dto.queue;

import java.time.LocalDateTime;

public record TokenActiveResDto(
        Long tokenId,
        Long userId,
        Long concertId,
        String status,
        LocalDateTime updateAt
) {
}
