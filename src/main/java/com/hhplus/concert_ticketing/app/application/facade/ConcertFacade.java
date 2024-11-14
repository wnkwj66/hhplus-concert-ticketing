package com.hhplus.concert_ticketing.app.application.facade;

import com.hhplus.concert_ticketing.app.application.usecase.ConcertService;
import com.hhplus.concert_ticketing.app.application.usecase.ScheduleService;
import com.hhplus.concert_ticketing.app.application.usecase.SeatService;
import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import com.hhplus.concert_ticketing.app.domain.seat.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {
    private final ConcertService concertService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;

    // 콘서트 조회 메서드
    @Transactional
    public List<Concert> getConcertList() {
        return concertService.getConcertList(LocalDateTime.now(), LocalDate.now());
    }
    // 콘서트 회차 조회 메서드
    @Transactional
    public List<Schedule> getScheduleList(Long concertId) {
        return scheduleService.getConcertSchedules(concertId);
    }

    // 잔여좌석 조회 메서드
    @Transactional
    public List<Seat> getSeatList(Long concertId, Long scheduleId) {
        return seatService.getSeatList(concertId, scheduleId);
    }


}
