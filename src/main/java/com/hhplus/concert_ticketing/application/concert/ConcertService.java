package com.hhplus.concert_ticketing.application.concert;


import com.hhplus.concert_ticketing.api.concert.dto.ConcertInfo;
import com.hhplus.concert_ticketing.api.concert.dto.PerformanceResDto;
import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.domain.concert.Seat;

import java.util.List;

public interface ConcertService {

    List<ConcertInfo> findConcertList();

    PerformanceResDto findPerformances(Long concertId);

    void save(Concert concertReqInfo);

    List<Seat> getAvailableSeatList(Long concertId, Long performanceId);

    Seat requestSeat(String uuid, Long seatId);
}
