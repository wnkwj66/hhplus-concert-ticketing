package com.hhplus.concert_ticketing.interfaces.dto.payment;

import java.time.LocalDateTime;

public record PaymentResDto(
        Long userId,
        Integer amount,
        LocalDateTime payedAt
) {
}
