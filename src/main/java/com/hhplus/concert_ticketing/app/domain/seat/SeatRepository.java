package com.hhplus.concert_ticketing.app.domain.seat;

import java.util.List;

public interface SeatRepository {

    List<Seat> findByScheduleIdAndStatusIn(Long scheduleId);

    Seat findById(Long seatId);

    void save(Seat seat);
}
