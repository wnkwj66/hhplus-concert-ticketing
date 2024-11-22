package com.hhplus.concert_ticketing.app.application.event.payment;

import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.infra.kafka.SimpleProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentEventHandler {
    private final SimpleProducer simpleProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        Payment payment = event.payment();
        simpleProducer.sendMessage("payment-topic", payment.getId().toString(), payment.toString());
    }
}
