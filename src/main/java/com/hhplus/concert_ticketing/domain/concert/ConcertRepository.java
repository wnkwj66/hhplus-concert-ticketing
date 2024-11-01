package com.hhplus.concert_ticketing.domain.concert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository {

    List<Concert> selectConcertList(LocalDateTime now , LocalDate now1);

//    List<ConcertPerformance> selectAvailablePerformance(Long concertId, int availableSeat, ConcertStatus status);
    List<ConcertPerformance> selectAvailablePerformance(Long concertId, int availableSeat);

    List<Seat> selectAvailableSeats(Long performanceId);

    Seat findByIdWithLock(Long seatId);

    Concert findById(Long concertId);

    List<ConcertPerformance> findByAvailableSeatGreaterThanOrStatusNot(int availableSeat, ConcertStatus soldOut);

    Concert save(Concert concert);
}
