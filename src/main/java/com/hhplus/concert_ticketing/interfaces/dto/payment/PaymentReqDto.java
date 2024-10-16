package com.hhplus.concert_ticketing.interfaces.dto.payment;

public record PaymentReqDto(
        Long userId,
        Long reservationId
) {
}
