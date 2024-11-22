package com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto;

import com.hhplus.concert_ticketing.app.domain.payment.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentRes(
        Long id,
        Integer amount,
        PaymentStatus status,
        LocalDateTime createAt
) {
}
