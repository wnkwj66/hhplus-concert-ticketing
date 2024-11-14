package com.hhplus.concert_ticketing.app.application.facade;

import com.hhplus.concert_ticketing.app.application.usecase.ReservationService;
import com.hhplus.concert_ticketing.app.application.usecase.UserService;
import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.domain.reservation.ReservationRepository;
import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.PointRepository;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.infra.user.JpaPointRepository;
import com.hhplus.concert_ticketing.app.infra.user.JpaUsersRepository;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto.PaymentReq;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentFacadeTest {
    @Autowired
    private PaymentFacade paymentFacade;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;
    @Autowired
    private JpaUsersRepository jpaUserRepository;
    @Autowired
    private JpaPointRepository jpaPointRepository;
    @Autowired
    private ReservationRepository reservationRepository;


    @AfterEach
    void cleanUp() {
        jpaUserRepository.deleteAll();
        jpaPointRepository.deleteAll();
    }

    @Transactional
    @Test
    void 결제_서비스_성공(){
        // given
        Users user = userService.createUser("김원중");
        Point point = pointRepository.findById(user.getId());
        point.chargePoint(50000);
        pointRepository.save(point);
        PaymentReq request = new PaymentReq(1L, user.getId());

        // when
        Payment payment = paymentFacade.paymentReservation(request);

        // Then: 결과 검증
        assertNotNull(payment);
        assertEquals(25000,point.getAmount());
    }
}