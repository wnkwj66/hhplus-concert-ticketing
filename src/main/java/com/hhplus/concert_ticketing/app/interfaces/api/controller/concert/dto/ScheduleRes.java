package com.hhplus.concert_ticketing.app.interfaces.api.controller.concert.dto;

import com.hhplus.concert_ticketing.app.domain.concert.ConcertStatus;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ScheduleRes(
        Long id,
        Long concertId,
        ConcertStatus status,
        LocalDateTime performanceAt,
        Integer availableSeat,
        Integer totalSeat
) {

    public static List<ScheduleRes> of(List<Schedule> resultList) {
        return resultList.stream()
                .map(result -> new ScheduleRes(
                        result.getId(),
                        result.getConcertId(),
                        result.getStatus(),
                        result.getPerformanceAt(),
                        result.getAvailableSeat(),
                        result.getTotalSeat()
                ))
                .collect(Collectors.toList());
    }
}
