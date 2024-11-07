package com.hhplus.concert_ticketing.app.interfaces.api.mapper;

import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.payment.PaymentRes;
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
