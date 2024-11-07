package com.hhplus.concert_ticketing.app.interfaces.api.mapper;

import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import com.hhplus.concert_ticketing.app.domain.concert.ConcertPerformance;
import com.hhplus.concert_ticketing.app.domain.concert.Reservation;
import com.hhplus.concert_ticketing.app.domain.concert.Seat;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.concert.ReservationRes;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.concert.SelectPerformanceRes;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.concert.SelectConcertRes;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.concert.SelectSeatRes;
import org.springframework.stereotype.Component;

@Component
public class ConcertMapper {

    public SelectConcertRes toConcertResponse(Concert concert) {
        return new SelectConcertRes(
                concert.getId(),
                concert.getTitle(),
                concert.getReservationStartAt(),
                concert.getStartDate(),
                concert.getEndDate()
        );
    }

    public SelectPerformanceRes toPerformanceResponce(ConcertPerformance performance) {
        return new SelectPerformanceRes(
                performance.getId(),
                performance.getStatus(),
                performance.getPerformanceAt(),
                performance.getAvailableSeat(),
                performance.getTotalSeat()
        );
    }

    public SelectSeatRes toSeatResponce(Seat seat) {
        return new SelectSeatRes(
                seat.getId(),
                seat.getPerformanceId(),
                seat.getSeatNo(),
                seat.getStatus(),
                seat.getExpiredAt()
        );
    }

    public ReservationRes toResrvationResponse(Reservation reservation) {
        return new ReservationRes(
                reservation.getId(),
                reservation.getSeatId(),
                reservation.getStatus(),
                reservation.getReservationAt(),
                reservation.getCreateAt()
        );
    }
}
