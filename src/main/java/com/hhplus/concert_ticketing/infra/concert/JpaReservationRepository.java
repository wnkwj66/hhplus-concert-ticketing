package com.hhplus.concert_ticketing.infra.concert;

import com.hhplus.concert_ticketing.domain.concert.Reservation;

public interface JpaReservationRepository {
    void save(Reservation reservation);

    Reservation findById(Long reservationId);
}
