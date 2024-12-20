package com.hhplus.concert_ticketing.app.application.facade;

import com.hhplus.concert_ticketing.app.application.usecase.PaymentService;
import com.hhplus.concert_ticketing.app.application.usecase.ReservationService;
import com.hhplus.concert_ticketing.app.application.usecase.UserService;
import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto.PaymentReq;
import com.hhplus.concert_ticketing.app.interfaces.exception.ApiException;
import com.hhplus.concert_ticketing.app.interfaces.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentFacade {
    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final UserService usersService;

    @Transactional
    public Payment paymentReservation(PaymentReq request) {
        // 결제요청 검증(예약 정보 조회, 올바른 예약인지 확인)
        Reservation reserveInfo = reservationService.getVerifyReservation(request.reservationId());
        // 유저 포인트차감 (유저 포인트 조회, 예약 금액 차감) -> 금액 부족하면 충전
        Users user = usersService.getUserById(request.userId());
        Point point = usersService.getPointByUserId(user.getPointId());
        usersService.deductPoint(user.getId(), reserveInfo.getTotalAmount());

        // 결제 정보 저장
        Payment payment = new Payment(user.getId(), reserveInfo.getId(), reserveInfo.getTotalAmount());
        paymentService.save(payment);

        // 결제 정보 전송 (외부 API)
        payment.finishPayment();
        return payment;
    }
}
