package com.hhplus.concert_ticketing.domain.concert;

public interface ConcertPerformanceRepository {
    ConcertPerformance findById(Long performanceId);
}
