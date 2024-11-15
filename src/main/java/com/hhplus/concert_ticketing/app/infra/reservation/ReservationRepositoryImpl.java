package com.hhplus.concert_ticketing.app.infra.reservation;

import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.domain.reservation.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public Reservation findById(Long id) {
        return jpaReservationRepository.findById(id).orElseThrow(()->new EntityNotFoundException("예약정보를 찾을 수 없습니다."));
    }

    @Override
    public int countByPerformanceIdAndSeatId(Long performanceId, Long seatId) {
        return jpaReservationRepository.countByPerformanceIdAndSeatId(performanceId,seatId);
    }
}
