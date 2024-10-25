package com.hhplus.concert_ticketing.infra.payment;

import com.hhplus.concert_ticketing.domain.payment.Payment;
import com.hhplus.concert_ticketing.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;
    @Override
    public void save(Payment payment) {
        jpaPaymentRepository.save(payment);
    }
}
