package com.hhplus.concert_ticketing.app.domain.concert;

public interface ConcertPerformanceRepository {
    ConcertPerformance findById(Long performanceId);

    ConcertPerformance save(ConcertPerformance concertPerformance);
}
