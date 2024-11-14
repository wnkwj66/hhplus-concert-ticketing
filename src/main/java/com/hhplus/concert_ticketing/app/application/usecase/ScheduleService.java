package com.hhplus.concert_ticketing.app.application.usecase;

import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import com.hhplus.concert_ticketing.app.domain.concert.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ConcertService concertService;
    private final ScheduleRepository schduleRepository;

    public Schedule findById(Long scheduleId) {
        return schduleRepository.findById(scheduleId);
    }

    public List<Schedule> getConcertSchedules(Long concertId) {
        Concert concert = concertService.findById(concertId);
        // 콘서트 유효성 검증
        concert.vaildateConcert();

        return schduleRepository.findByConcertId(concertId);
    }

}
