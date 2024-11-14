package com.hhplus.concert_ticketing.app.interfaces.api.controller.concert.dto;

import com.hhplus.concert_ticketing.app.domain.concert.Concert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ConcertRes(Long id,
                         String title,
                         LocalDateTime reservationStartAt,
                         LocalDate startDate,
                         LocalDate endDate
) {

    public static List<ConcertRes> of(List<Concert> resultList) {
        return resultList.stream()
                .map(result -> new ConcertRes(
                        result.getId(),
                        result.getTitle(),
                        result.getReservationStartAt(),
                        result.getStartDate(),
                        result.getEndDate()
                ))
                .collect(Collectors.toList());
    }
}