package com.hhplus.concert_ticketing.infra.concert;

import com.hhplus.concert_ticketing.domain.concert.Seat;
import com.hhplus.concert_ticketing.domain.concert.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface JpaSeatRepository {
    List<Seat> findByPerformanceIdAndStatusIn(Long performanceId, SeatStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Seat findByIdWithLock(Long seatId);
}
