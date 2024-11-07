package com.hhplus.concert_ticketing.app.infra.repository.concert;

import com.hhplus.concert_ticketing.app.domain.concert.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaReservationRepository extends JpaRepository<Reservation,Long> {

    Optional<Reservation> findById(Long reservationId);

    int countByPerformanceIdAndSeatId(Long performanceId, Long seatId);
}
