package com.hhplus.concert_ticketing.api.queue.dto;

public record TokenValidateResDto(
        boolean isValid,
        Long tokenId,
        Long userId
){
}
