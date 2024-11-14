package com.hhplus.concert_ticketing.app.application.event.payment;

import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PaymentEventHandler {

    @TransactionalEventListener
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        Payment payment = event.getPayment();

        // 결제 상태 변경, 예약 상태 변경, 결제 정보 전송(외부 API 호출)
        System.out.println("결제 완료 후 작업을 수행합니다.");

        // 예: 예약 상태 업데이트 로직
        // reservationService.updateReservationStatus(payment.getReservationId(), newStatus);

        // 외부 API 전송 로직
        // externalApiService.sendPaymentInfo(payment);
    }
}
