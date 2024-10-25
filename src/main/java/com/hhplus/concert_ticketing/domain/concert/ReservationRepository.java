package com.hhplus.concert_ticketing.domain.concert;

public interface ReservationRepository {

    void save(Reservation reservation);

    Reservation findById(Long reservationId);
}
