package com.hhplus.concert_ticketing.infra.repository.concert;

import com.hhplus.concert_ticketing.domain.concert.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@NoRepositoryBean
public class ConcertRepositoryImpl implements ConcertRepository {

    private final JpaConcertRepository jpaConcertRepository;
    private final JpaPerformanceRepository jpaPerformanceRepository;
    private final JpaSeatRepository jpaSeatRepository;

    @Override
    public List<Concert> selectConcertList( LocalDateTime now, LocalDate now1){
        return jpaConcertRepository.selectConcertList(now,now1);
    }
/*

    @Override
    public List<ConcertPerformance> selectAvailablePerformance(Long concertId, int availableSeat, ConcertStatus status) {
        return jpaPerformanceRepository.findAvailablePerformances(concertId,availableSeat,status);
    }
*/
    @Override
    public List<ConcertPerformance> selectAvailablePerformance(Long concertId, int availableSeat) {
        return jpaPerformanceRepository.findByConcertIdAndAvailableSeatGreaterThan(concertId,availableSeat);
    }
    @Override
    public List<Seat> selectAvailableSeats(Long performanceId) {
        return jpaSeatRepository.findByPerformanceIdAndStatusIn(performanceId);
    }

    @Override
    public Seat findByIdWithLock(Long seatId) {
        return jpaSeatRepository.findByIdWithLock(seatId);
    }

    @Override
    public Concert findById(Long concertId) {
        return jpaConcertRepository.findById(concertId).orElse(null);
    }

    @Override
    public List<ConcertPerformance> findByAvailableSeatGreaterThanOrStatusNot(int availableSeat, ConcertStatus soldOut) {
        return jpaPerformanceRepository.findByAvailableSeatGreaterThanOrStatusNot(availableSeat,soldOut);
    }

}
