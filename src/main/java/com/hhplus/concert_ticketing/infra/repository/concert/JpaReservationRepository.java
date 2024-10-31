package com.hhplus.concert_ticketing.infra.repository.concert;

import com.hhplus.concert_ticketing.domain.concert.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaReservationRepository extends JpaRepository<Reservation,Long> {

    Optional<Reservation> findById(Long reservationId);
}
