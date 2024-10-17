package com.hhplus.concert_ticketing.api.queue.dto;

public record TokenStatusResDto(
        Long tokenId,
        Long userId,
        String status
) {
}
