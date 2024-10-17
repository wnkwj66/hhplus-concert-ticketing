package com.hhplus.concert_ticketing.api.payment.dto;

import java.time.LocalDateTime;

public record PointChargeResDto(
        Long userId,
        Integer chargedAmount,
        LocalDateTime chargedAt
) {
}
