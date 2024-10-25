package com.hhplus.concert_ticketing.interfaces.api.controller.dto.payment;

import com.hhplus.concert_ticketing.domain.payment.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentRes(
        Long id,
        Integer amount,
        PaymentStatus status,
        LocalDateTime createAt
) {
}
