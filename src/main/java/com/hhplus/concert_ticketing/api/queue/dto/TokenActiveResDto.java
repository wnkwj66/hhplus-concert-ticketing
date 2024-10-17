package com.hhplus.concert_ticketing.api.queue.dto;

import java.time.LocalDateTime;

public record TokenActiveResDto(
        Long tokenId,
        Long userId,
        Long concertId,
        String status,
        LocalDateTime updateAt
) {
}
