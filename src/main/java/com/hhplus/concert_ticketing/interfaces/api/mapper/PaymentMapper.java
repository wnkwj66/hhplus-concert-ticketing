package com.hhplus.concert_ticketing.interfaces.api.mapper;

import com.hhplus.concert_ticketing.domain.payment.Payment;
import com.hhplus.concert_ticketing.interfaces.api.controller.dto.payment.PaymentRes;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {
    public PaymentRes toPaymentResponse(Payment payment) {
        return new PaymentRes(
                payment.getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getCreateAt()
        );
    }
}
