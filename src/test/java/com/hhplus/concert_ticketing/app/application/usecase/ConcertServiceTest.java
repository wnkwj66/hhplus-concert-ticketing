package com.hhplus.concert_ticketing.app.application.usecase;

import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import com.hhplus.concert_ticketing.app.domain.concert.ConcertRepository;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import com.hhplus.concert_ticketing.app.domain.concert.ScheduleRepository;
import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.domain.reservation.ReservationRepository;
import com.hhplus.concert_ticketing.app.domain.seat.Seat;
import com.hhplus.concert_ticketing.app.domain.seat.SeatRepository;
import com.hhplus.concert_ticketing.app.domain.seat.SeatStatus;
import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.PointRepository;
import com.hhplus.concert_ticketing.app.domain.user.UserRepository;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.infra.user.JpaPointRepository;
import com.hhplus.concert_ticketing.app.infra.user.JpaUsersRepository;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.reservation.dto.ReserveReq;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcertServiceTest {
    @Autowired private ConcertRepository concertRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PointRepository pointRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private ReservationService reservationService;
    @Autowired private UserService userService;
    @Autowired private JpaUsersRepository jpaUserRepository;
    @Autowired private JpaPointRepository jpaPointRepository;

    @AfterEach
    void cleanUp() {
        jpaUserRepository.deleteAll();
        jpaPointRepository.deleteAll();
    }


    @Test
    void 예약_서비스_성공(){
        // given
        Users user = userService.createUser("김원중");
        ReserveReq request = new ReserveReq(1L,1L,2L);
        Point point = pointRepository.findById(user.getId());

        // when
        Reservation reservation = reservationService.reserveConcert(request);

        // Then: 결과 검증
        assertNotNull(reservation);
    }



}