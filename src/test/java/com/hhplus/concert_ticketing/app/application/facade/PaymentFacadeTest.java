package com.hhplus.concert_ticketing.app.application.facade;

import com.hhplus.concert_ticketing.app.application.event.payment.PaymentCompletedEvent;
import com.hhplus.concert_ticketing.app.application.event.payment.PaymentFailedEvent;
import com.hhplus.concert_ticketing.app.application.usecase.ReservationService;
import com.hhplus.concert_ticketing.app.application.usecase.UserService;
import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.domain.payment.PaymentRepository;
import com.hhplus.concert_ticketing.app.domain.payment.PaymentStatus;
import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.domain.reservation.ReservationRepository;
import com.hhplus.concert_ticketing.app.domain.reservation.ReservationStatus;
import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.PointRepository;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.infra.user.JpaPointRepository;
import com.hhplus.concert_ticketing.app.infra.user.JpaUsersRepository;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto.PaymentReq;
import com.hhplus.concert_ticketing.app.interfaces.exception.ApiException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private PaymentRepository paymentRepository;

    private long userId;

    @BeforeEach
    void setup() {
        Users user = userService.createUser("김원중");
        Point point = pointRepository.findById(user.getId());
        point.chargePoint(50000);
        pointRepository.save(point);
        userId = user.getId();

    }
    @AfterEach
    void cleanUp() {
        jpaUserRepository.deleteAll();
        jpaPointRepository.deleteAll();
    }
    @Test
    void 결제완료_이벤트_처리_예약상태_변경() {
        // 결제 요청 및 결제 완료 이벤트 발행
        PaymentReq request = new PaymentReq(1L, userId);
        paymentFacade.paymentReservation(request);

        // 예약 상태가 "RESERVED"로 변경되었는지 검증
        Reservation updatedReservation = reservationRepository.findById(1L);
        assertEquals(ReservationStatus.RESERVED, updatedReservation.getStatus());
    }
    @Test
    void 결제_완료_이벤트_실패_시_결제_실패_이벤트_발행() {
        // given
        PaymentReq request = new PaymentReq(2L, userId);
        // when & then
        ApiException e = assertThrows(ApiException.class, () -> paymentFacade.paymentReservation(request));
        assertThat(e.getMessage()).isEqualTo("예약이 취소된 상태입니다.");
    }

}