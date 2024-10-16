package com.hhplus.concert_ticketing.interfaces.dto.payment;

public record PointReqDto(
        Long userId,
        String type,
        Integer amount
) {
}
