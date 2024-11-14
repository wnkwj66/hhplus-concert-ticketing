package com.hhplus.concert_ticketing.app.interfaces.api.controller.reservation;

import com.hhplus.concert_ticketing.app.application.facade.ReservationFacade;
import com.hhplus.concert_ticketing.app.domain.reservation.Reservation;
import com.hhplus.concert_ticketing.app.interfaces.api.common.ApiResponse;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.reservation.dto.ReserveReq;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.reservation.dto.ReserveRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "콘서트 예매 API", description = "콘서트 예매와 관련된 API 입니다. 모든 API는 대기열 토큰 헤더(Authorization) 가 필요합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concerts")
public class ReservationController {
    private final ReservationFacade reservationFacade;

    @Operation(summary = "콘서트 예약 API" , description = "콘서트 좌석을 임시로 예약합니다.")
    @PostMapping("/reserve")
    public ApiResponse<ReserveRes> reserve(@RequestBody ReserveReq request) {

        Reservation reservation = reservationFacade.reserveConcert(request);

        return ApiResponse.success(ReserveRes.of(reservation));

    }
}
