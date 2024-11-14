package com.hhplus.concert_ticketing.app.domain.reservation;

public interface ReservationRepository {

    public void save(Reservation reservation);

    public Reservation findById(Long reservationId);

    public int countByPerformanceIdAndSeatId(Long performanceId, Long seatId);
}
