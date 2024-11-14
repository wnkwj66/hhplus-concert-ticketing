package com.hhplus.concert_ticketing.app.application.usecase;

import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.domain.reservation.ReservationRepository;
import com.hhplus.concert_ticketing.app.domain.seat.Seat;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.reservation.dto.ReserveReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ConcertService concertService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Reservation reserveConcert(ReserveReq request) {
        Concert concert = concertService.findById(request.concertId());
        concert.vaildateConcert();

        Schedule schedule = scheduleService.findById(request.scheduleId());
        schedule.isSoldOutCheck();

        Seat seat = seatService.findById(request.seatId());
        seat.isReservedCheck();

        Reservation reservation = new Reservation(1L,concert, schedule, seat);
        reservationRepository.save(reservation);

        return reservation;
    }


    public Reservation getVerifyReservation(Long reserveId) {
        Reservation reservation = reservationRepository.findById(reserveId);
        reservation.isValidReservation();

        return reservation;
    }
}
