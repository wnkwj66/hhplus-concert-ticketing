package com.hhplus.concert_ticketing.app.infra.payment;

import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<Payment,Long> {
}
