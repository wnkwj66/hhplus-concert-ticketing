package com.hhplus.concert_ticketing.app.application.event.payment;


import com.hhplus.concert_ticketing.app.domain.payment.Payment;

public record PaymentCompletedEvent(
        Payment payment
) {


}
