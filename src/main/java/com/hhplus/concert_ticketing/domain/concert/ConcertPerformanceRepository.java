package com.hhplus.concert_ticketing.domain.concert;

import java.util.Optional;

public interface ConcertPerformanceRepository {
    ConcertPerformance findById(Long performanceId);

    ConcertPerformance save(ConcertPerformance concertPerformance);
}
