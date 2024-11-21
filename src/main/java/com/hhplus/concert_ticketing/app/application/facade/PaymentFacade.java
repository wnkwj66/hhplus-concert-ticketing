package com.hhplus.concert_ticketing.app.application.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert_ticketing.app.application.event.payment.PaymentCompletedEvent;
import com.hhplus.concert_ticketing.app.application.event.payment.PaymentFailedEvent;
import com.hhplus.concert_ticketing.app.application.usecase.OutboxEventService;
import com.hhplus.concert_ticketing.app.application.usecase.PaymentService;
import com.hhplus.concert_ticketing.app.application.usecase.ReservationService;
import com.hhplus.concert_ticketing.app.application.usecase.UserService;
import com.hhplus.concert_ticketing.app.domain.outbox.OutboxEvent;
import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.domain.payment.PaymentStatus;
import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto.PaymentReq;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto.PaymentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final UserService usersService;
    private final ApplicationEventPublisher eventPublisher;
    private final OutboxEventService outboxEventService;

    @Transactional
    public Payment paymentReservation(PaymentReq request) {
        // 결제요청 검증(예약 정보 조회, 올바른 예약인지 확인)
        Reservation reserveInfo = reservationService.getVerifyReservation(request.reservationId());
        // 유저 포인트차감 (유저 포인트 조회, 예약 금액 차감) -> 금액 부족하면 충전
        Users user = usersService.getUserById(request.userId());
        Point point = usersService.getPointByUserId(user.getPointId());
        usersService.deductPoint(user.getId(), reserveInfo.getTotalAmount());

        // 결제 정보 저장
        Payment payment = new Payment(user.getId(), reserveInfo.getId(), reserveInfo.getTotalAmount());
        payment.finishPayment();
        reserveInfo.finishReservation();
        // paymentService.save(payment);
        try {
            // 아웃박스 저장 (이벤트를 데이터베이스에 먼저 기록)
            PaymentRes paymentRes = new PaymentRes(payment.getId(), payment.getAmount(), PaymentStatus.DONE);
            ObjectMapper objectMapper = new ObjectMapper();
            String eventMessage = objectMapper.writeValueAsString(paymentRes);
            OutboxEvent outboxEvent = new OutboxEvent("PaymentCompletedEvent",eventMessage);
            outboxEventService.save(outboxEvent);
        } catch (Exception e) {
            throw new IllegalArgumentException("아웃박스 발행 예외 발생: " + e.getMessage());
        }

        // 결제 완료 이벤트 발행
        eventPublisher.publishEvent(new PaymentCompletedEvent(payment));
        return payment;
    }

//    @Transactional
//    public paymentOutboxEvent()
}