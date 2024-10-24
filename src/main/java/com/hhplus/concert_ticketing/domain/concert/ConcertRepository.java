package com.hhplus.concert_ticketing.domain.concert;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository {

    List<Concert> selectConcertList(LocalDateTime now);

    List<ConcertPerformance> selectAvailablePerformance(Long concertId, int availableSeat, ConcertStatus status);

    List<Seat> selectAvailableSeats(Long performanceId, SeatStatus status);
}
