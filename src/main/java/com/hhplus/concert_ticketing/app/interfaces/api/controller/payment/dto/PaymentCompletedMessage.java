package com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto;

public record PaymentCompletedMessage(
        Long id,
        Long reservationId,
        Integer amount
) {
}
