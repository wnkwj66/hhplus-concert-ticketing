package com.hhplus.concert_ticketing.app.interfaces.api.controller.concert.dto;

import com.hhplus.concert_ticketing.app.domain.seat.Seat;
import com.hhplus.concert_ticketing.app.domain.seat.SeatStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record SeatRes(
        Long id,
        Long scheduleId,
        Integer price,
        SeatStatus status,
        LocalDateTime expirdeAt
) {
    public static List<SeatRes> of(List<Seat> resultList) {
        return resultList.stream()
                .map(result -> new SeatRes(
                        result.getId(),
                        result.getScheduleId(),
                        result.getPrice(),
                        result.getStatus(),
                        result.getExpiredAt()
                ))
                .collect(Collectors.toList());
    }
}
