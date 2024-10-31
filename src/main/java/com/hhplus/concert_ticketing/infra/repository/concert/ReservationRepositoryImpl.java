package com.hhplus.concert_ticketing.infra.repository.concert;

import com.hhplus.concert_ticketing.domain.concert.Reservation;
import com.hhplus.concert_ticketing.domain.concert.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {
    private final JpaReservationRepository jpaReservationRepository;

    @Override
    public void save(Reservation reservation) {
        jpaReservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long reservationId) {
        return jpaReservationRepository.findById(reservationId).orElse(null);
    }
}
