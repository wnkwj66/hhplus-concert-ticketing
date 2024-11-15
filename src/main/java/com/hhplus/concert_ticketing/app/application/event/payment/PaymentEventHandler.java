package com.hhplus.concert_ticketing.app.application.event.payment;

import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.domain.payment.PaymentRepository;
import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.domain.reservation.ReservationRepository;
import com.sun.jdi.event.ExceptionEvent;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentEventHandler {

    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private boolean isSuccess = false;


    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void handlePaymentCompleted(PaymentCompletedEvent event) throws InterruptedException {
        Payment payment = event.payment();

        // 결제 상태 변경, 예약 상태 변경, 결제 정보 전송(외부 API 호출)
        System.out.println("결제 완료 후 작업을 수행합니다.");

        // 예약 상태 업데이트 로직
        Reservation reservation = reservationRepository.findById(payment.getReservationId());
        reservation.finishReservation();
        reservationRepository.save(reservation);
        // 외부 API 전송 로직
        externalApi();
    }


    public void externalApi() throws InterruptedException {

    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @EventListener
    public void handlePaymentFailed(PaymentFailedEvent event) {
        // 실패시 결제 내역 삭제
        Payment payment = event.payment();
        // 결제 상태를 "실패"로 변경
        payment.markAsFailed();
        paymentRepository.save(payment);
    }
}
