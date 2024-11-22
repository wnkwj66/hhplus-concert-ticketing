package com.hhplus.concert_ticketing.app.application.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhplus.concert_ticketing.app.application.event.payment.PaymentCompletedEvent;
import com.hhplus.concert_ticketing.app.application.usecase.OutboxEventService;
import com.hhplus.concert_ticketing.app.application.usecase.PaymentService;
import com.hhplus.concert_ticketing.app.application.usecase.ReservationService;
import com.hhplus.concert_ticketing.app.application.usecase.UserService;
import com.hhplus.concert_ticketing.app.domain.outbox.OutboxEvent;
import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto.PaymentCompletedMessage;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto.PaymentReq;
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
        // 1. 결제 요청 검증
        Reservation reserveInfo = reservationService.getVerifyReservation(request.reservationId());
        // 2. 유저 포인트 차감
        Users user = usersService.getUserById(request.userId());
        Point point = usersService.getPointByUserId(user.getPointId());
        usersService.deductPoint(user.getId(), reserveInfo.getTotalAmount());

        // 3. 결제 정보 저장
        Payment payment = new Payment(user.getId(), reserveInfo.getId(), reserveInfo.getTotalAmount());
        payment.finishPayment();
        reserveInfo.finishReservation();
        payment = paymentService.save(payment);

        // 4. 아웃박스 이벤트 생성 및 저장
        try {
            PaymentCompletedMessage message = new PaymentCompletedMessage(
                    payment.getId(),
                    payment.getReservationId(),
                    payment.getAmount()
            );
            // ObjectMapper를 사용하여 JSON 문자열 생성
            ObjectMapper objectMapper = new ObjectMapper();
            String eventMessage = objectMapper.writeValueAsString(message);
            // 아웃박스 이벤트 저장
            OutboxEvent outboxEvent = new OutboxEvent("PaymentCompletedEvent",eventMessage);
            outboxEventService.save(outboxEvent);
        } catch (Exception e) {
            throw new RuntimeException("아웃박스 이벤트 저장 실패로 트랜잭션 롤백: " + e.getMessage());
        }

        // 5. 이벤트 퍼블리셔 발행
        eventPublisher.publishEvent(new PaymentCompletedEvent(payment));
        return payment;
    }


    @Transactional
    public void paymentOutboxEvent(String message){
        OutboxEvent event = outboxEventService.findByMessage(message);
        event.markAsPublish();
        outboxEventService.save(event);
    }
}