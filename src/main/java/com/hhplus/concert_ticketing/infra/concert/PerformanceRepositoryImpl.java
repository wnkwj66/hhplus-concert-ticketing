package com.hhplus.concert_ticketing.infra.concert;

import com.hhplus.concert_ticketing.domain.concert.ConcertPerformance;
import com.hhplus.concert_ticketing.domain.concert.ConcertPerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PerformanceRepositoryImpl implements ConcertPerformanceRepository {
    private final JpaPerformanceRepository jpaPerformanceRepository;

    @Override
    public ConcertPerformance findById(Long performanceId) {
        return jpaPerformanceRepository.findById(performanceId);
    }
}
