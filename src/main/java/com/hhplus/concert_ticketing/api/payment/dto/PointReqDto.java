package com.hhplus.concert_ticketing.api.payment.dto;

public record PointReqDto(
        Long userId,
        String type,
        Integer amount
) {
}
