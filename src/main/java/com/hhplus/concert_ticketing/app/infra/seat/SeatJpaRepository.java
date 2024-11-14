package com.hhplus.concert_ticketing.app.infra.seat;

import com.hhplus.concert_ticketing.app.domain.seat.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<Seat,Long> {
    @Query("SELECT s FROM Seat s WHERE s.status = 'AVAILABLE' AND s.scheduleId = :scheduleId")
    List<Seat> findByScheduleIdAndStatusIn(@Param("scheduleId") Long scheduleId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    Seat findByIdWithLock(@Param("seatId") Long seatId);

}
