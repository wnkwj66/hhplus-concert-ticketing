package com.hhplus.concert_ticketing.infra.concert;


import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.domain.concert.Performance;
import com.hhplus.concert_ticketing.domain.concert.Seat;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository  {
    void save(Concert concertReqInfo);

    Seat save(Seat seat);

    List<Concert> getConcertList();

    Optional<Concert> findById(Long concertId);

    List<Performance> getPerformanceList(Long concertId);

    List<Seat> getAvailableSeatList(Long performanceId);

    Optional<Seat> getSeatById(Long seatId);
}
