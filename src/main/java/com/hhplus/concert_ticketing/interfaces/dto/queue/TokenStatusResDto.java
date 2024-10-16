package com.hhplus.concert_ticketing.interfaces.dto.queue;

public record TokenStatusResDto(
        Long tokenId,
        Long userId,
        String status
) {
}
