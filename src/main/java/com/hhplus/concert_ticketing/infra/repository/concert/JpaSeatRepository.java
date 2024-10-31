package com.hhplus.concert_ticketing.infra.repository.concert;

import com.hhplus.concert_ticketing.domain.concert.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaSeatRepository extends JpaRepository<Seat,Long> {
    @Query("SELECT s FROM Seat s WHERE s.status = 'AVAILABLE' AND s.performanceId = :performanceId")
    List<Seat> findByPerformanceIdAndStatusIn(@Param("performanceId") Long performanceId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    Seat findByIdWithLock(@Param("seatId") Long seatId);

}
