package com.hhplus.concert_ticketing.app.infra.payment;

import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.domain.payment.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
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

    @Override
    public Payment findById(long id) {
        return jpaPaymentRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("결제정보를 찾을 수 없습니다."));
    }
}
