package com.hhplus.concert_ticketing.app.application.event.payment;

import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class PaymentCompletedEvent {
    @Getter
    private final Payment payment;

}
