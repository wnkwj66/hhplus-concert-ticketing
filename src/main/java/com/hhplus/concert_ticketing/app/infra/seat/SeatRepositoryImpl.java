package com.hhplus.concert_ticketing.app.infra.seat;

import com.hhplus.concert_ticketing.app.domain.seat.Seat;
import com.hhplus.concert_ticketing.app.domain.seat.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {
    private final SeatJpaRepository seatJpaRepository;

    @Override
    public Seat findById(Long seatId) {
        return seatJpaRepository.findById(seatId).orElseThrow(() -> new EntityNotFoundException("Seat not found"));
    }

    @Override
    public void save(Seat seat) {
        seatJpaRepository.save(seat);
    }

    @Override
    public List<Seat> findByScheduleIdAndStatusIn(Long scheduleId) {
        return seatJpaRepository.findByScheduleIdAndStatusIn(scheduleId);
    }

}
