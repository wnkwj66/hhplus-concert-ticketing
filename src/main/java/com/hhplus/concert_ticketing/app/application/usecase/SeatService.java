package com.hhplus.concert_ticketing.app.application.usecase;

import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import com.hhplus.concert_ticketing.app.domain.seat.Seat;
import com.hhplus.concert_ticketing.app.domain.seat.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {
    private final ConcertService concertService;
    private final ScheduleService scheduleService;
    private final SeatRepository seatRepository;

    public Seat findById(Long seatId) {
        return seatRepository.findById(seatId);
    }

    public List<Seat> getSeatList(Long concertId, Long scheduleId) {
        // 콘서트 유효성 검증
        Concert concert = concertService.findById(concertId);
        concert.vaildateConcert();

        // 콘서트 회차 유효성 검증(매진 상태)
        Schedule schedule = scheduleService.findById(scheduleId);
        schedule.isSoldOutCheck();

        return seatRepository.findByScheduleIdAndStatusIn(scheduleId);
    }

}
