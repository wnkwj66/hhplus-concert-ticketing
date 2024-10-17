package com.hhplus.concert_ticketing.api.payment.dto;

import java.time.LocalDateTime;

public record PaymentResDto(
        Long userId,
        Integer amount,
        LocalDateTime payedAt
) {
}
