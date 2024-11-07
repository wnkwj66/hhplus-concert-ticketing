package com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.concert;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record SelectConcertRes(Long id,
                               String title,
                               LocalDateTime reservationStartAt,
                               LocalDate startDate,
                               LocalDate endDate
) {
}