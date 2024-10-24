package com.hhplus.concert_ticketing.domain.concert;

import com.hhplus.concert_ticketing.domain.concert.Seat;
import com.hhplus.concert_ticketing.domain.concert.SeatStatus;

import java.util.List;

public interface JpaSeatRepository {
    List<Seat> findByPerformanceIdAndStatusIn(Long performanceId, SeatStatus status);
}
