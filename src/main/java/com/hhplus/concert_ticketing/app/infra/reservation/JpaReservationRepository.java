package com.hhplus.concert_ticketing.app.infra.reservation;

import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaReservationRepository extends JpaRepository<Reservation,Long> {

    Optional<Reservation> findById(Long id);

    int countByPerformanceIdAndSeatId(Long performanceId, Long seatId);
}
