package com.hhplus.concert_ticketing.interfaces.dto.queue;

public record TokenValidateResDto(
        boolean isValid,
        Long tokenId,
        Long userId
){
}
