package com.hhplus.concert_ticketing.infra.concert;

import com.hhplus.concert_ticketing.domain.concert.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final JpaConcertRepository jpaConcertRepository;
    private final JpaPerformanceRepository jpaPerformanceRepository;
    private final JpaSeatRepository jpaSeatRepository;

    @Override
    public List<Concert> selectConcertList(LocalDateTime now){
        return jpaConcertRepository.selectConcertList(now);
    }

    @Override
    public List<ConcertPerformance> selectAvailablePerformance(Long concertId, int availableSeat, ConcertStatus status) {
        return jpaPerformanceRepository.findByConcertIdAndAvailableSeatsGreaterThanAndStatusIn(concertId,availableSeat,status);
    }

    @Override
    public List<Seat> selectAvailableSeats(Long performanceId, SeatStatus status) {
        return jpaSeatRepository.findByPerformanceIdAndStatusIn(performanceId,status);
    }

    @Override
    public Seat findByIdWithLock(Long seatId) {
        return jpaSeatRepository.findByIdWithLock(seatId);
    }

    @Override
    public Concert findById(Long concertId) {
        return jpaConcertRepository.findById(concertId).orElse(null);
    }

}
