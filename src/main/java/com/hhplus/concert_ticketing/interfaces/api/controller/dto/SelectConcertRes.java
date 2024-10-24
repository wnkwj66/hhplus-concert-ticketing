package com.hhplus.concert_ticketing.interfaces.api.controller.dto;

import com.hhplus.concert_ticketing.domain.concert.Concert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public record SelectConcertRes(Long id,
                               String title,
                               LocalDateTime reservationStartAt,
                               LocalDate startDate,
                               LocalDate endDate
) {
}