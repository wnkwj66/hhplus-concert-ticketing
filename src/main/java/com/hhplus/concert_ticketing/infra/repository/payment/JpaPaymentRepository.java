package com.hhplus.concert_ticketing.infra.repository.payment;

import com.hhplus.concert_ticketing.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<Payment,Long> {
}
