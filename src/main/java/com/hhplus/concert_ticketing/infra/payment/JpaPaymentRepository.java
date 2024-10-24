package com.hhplus.concert_ticketing.infra.payment;

import com.hhplus.concert_ticketing.domain.payment.Payment;

public interface JpaPaymentRepository {
    void save(Payment payment);
}
