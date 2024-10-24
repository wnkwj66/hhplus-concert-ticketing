package com.hhplus.concert_ticketing.infra.concert;

import com.hhplus.concert_ticketing.domain.concert.ConcertStatus;
import com.hhplus.concert_ticketing.domain.concert.ConcertPerformance;

import java.util.List;

public interface JpaPerformanceRepository {
    List<ConcertPerformance> findByConcertIdAndAvailableSeatsGreaterThanAndStatusIn(Long concertId, int availableSeat, ConcertStatus status);

    ConcertPerformance findById(Long performanceId);
}
