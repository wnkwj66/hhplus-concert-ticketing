package com.hhplus.concert_ticketing.app.interfaces.api.controller.payment;

import com.hhplus.concert_ticketing.app.application.facade.PaymentFacade;
import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.interfaces.api.common.ApiResponse;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto.PaymentReq;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.payment.dto.PaymentRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제 API", description = "결제와 관련된 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
public class PaymentContorller {
    private final PaymentFacade paymentFacade;

    @Operation(summary = "결제 요청 API",description = "결제 되면 좌석 변경 및 예약 상태 변환")
    @PostMapping("/payment")
    public ApiResponse<PaymentRes> paymentConcert(
//            @RequestHeader("Authorization") String token,
            @RequestBody PaymentReq request) {

        Payment payment = paymentFacade.paymentReservation(request);
        return ApiResponse.success(null);
    }
}
