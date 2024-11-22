package com.hhplus.concert_ticketing.app.domain.payment;

public interface PaymentRepository {
    Payment save(Payment payment);

    Payment findById(long id);
}
