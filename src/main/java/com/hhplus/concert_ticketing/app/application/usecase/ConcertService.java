package com.hhplus.concert_ticketing.app.application.usecase;

import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import com.hhplus.concert_ticketing.app.domain.concert.ConcertRepository;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import com.hhplus.concert_ticketing.app.domain.concert.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;
    private final ScheduleRepository scheduleRepository;

    public Concert findById(Long concertId) {
        return concertRepository.findById(concertId);
    }

    public List<Concert> getConcertList(LocalDateTime now, LocalDate now1) {
        return concertRepository.getConcertList(now,now1);
    }

    @Transactional(readOnly = true)
    public void validateConcertAndSchedule(Long concertId, Long scheduleId) {
        Concert concert = concertRepository.findById(concertId);
        concert.vaildateConcert();

        Schedule schedule = scheduleRepository.findById(scheduleId);
        schedule.isSoldOutCheck();

    }

}
