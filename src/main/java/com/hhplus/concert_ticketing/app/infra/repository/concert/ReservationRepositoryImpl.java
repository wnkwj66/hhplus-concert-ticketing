package com.hhplus.concert_ticketing.app.infra.repository.concert;

import com.hhplus.concert_ticketing.app.domain.concert.Reservation;
import com.hhplus.concert_ticketing.app.domain.concert.ReservationRepository;
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

    @Override
    public int countByPerformanceIdAndSeatId(Long performanceId, Long seatId) {
        return jpaReservationRepository.countByPerformanceIdAndSeatId(performanceId,seatId);
    }
}
