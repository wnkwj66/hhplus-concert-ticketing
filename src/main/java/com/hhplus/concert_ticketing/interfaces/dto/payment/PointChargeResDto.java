package com.hhplus.concert_ticketing.interfaces.dto.payment;

import java.time.LocalDateTime;

public record PointChargeResDto(
        Long userId,
        Integer chargedAmount,
        LocalDateTime chargedAt
) {
}
