package com.hhplus.concert_ticketing.api.payment.dto;

public record PaymentReqDto(
        Long userId,
        Long reservationId
) {
}
