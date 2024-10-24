package com.hhplus.concert_ticketing.application;

import com.hhplus.concert_ticketing.domain.concert.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertUseCase {
    private final ConcertRepository concertRepository;


    public List<Concert> selectConcertList() {
        return concertRepository.selectConcertList(LocalDateTime.now());
    }

    public List<ConcertPerformance> selectPerformances(Long concertId) {
        return concertRepository.selectAvailablePerformance(concertId, 0, ConcertStatus.AVAILABLE);
    }

    public List<Seat> selectSeats(Long performanceId) {
        return concertRepository.selectAvailableSeats(performanceId, SeatStatus.AVAILABLE);
    }
}
