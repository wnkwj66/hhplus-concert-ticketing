package com.hhplus.concert_ticketing.interfaces.api.controller;

import com.hhplus.concert_ticketing.application.ConcertUseCase;
import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.domain.concert.ConcertPerformance;
import com.hhplus.concert_ticketing.domain.concert.Reservation;
import com.hhplus.concert_ticketing.domain.concert.Seat;
import com.hhplus.concert_ticketing.domain.payment.Payment;
import com.hhplus.concert_ticketing.interfaces.api.common.ApiResponse;
import com.hhplus.concert_ticketing.interfaces.api.controller.dto.concert.*;
import com.hhplus.concert_ticketing.interfaces.api.controller.dto.payment.PaymentReq;
import com.hhplus.concert_ticketing.interfaces.api.controller.dto.payment.PaymentRes;
import com.hhplus.concert_ticketing.interfaces.api.mapper.ConcertMapper;
import com.hhplus.concert_ticketing.interfaces.api.mapper.PaymentMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "콘서트 API", description = "콘서트 예매와 관련된 API 입니다. 모든 API는 대기열 토큰 헤더(Authorization) 가 필요합니다.")
@RestController // @Controller와 다르게 반환하려는 주류는 JSON 형태 @Controller는 주로 view 반환
@RequiredArgsConstructor // DI(의존성 주입)의 방법 중에 생성자 주입을 임의의 코드없이 자동으로 설정
@RequestMapping("/api/v1/concerts") // REST Api의 버전관리
public class ConcertController {
    private final ConcertUseCase concertUseCase;
    private final ConcertMapper concertMapper;
    private final PaymentMapper paymentMapper;

    @Operation(summary = "콘서트 조회 API", description = "예약 가능한 콘서트 목록을 조회한다.")
    @GetMapping("/")
    public ApiResponse<List<SelectConcertRes>> selectConcert() {
        List<Concert> concertList = concertUseCase.selectConcertList();

        List<SelectConcertRes> response = concertList.stream()
                .map(concertMapper::toConcertResponse)
                .toList();

        return ApiResponse.success(response);
    }

    @Operation(summary = "콘서트 회차 조회 API", description = "예약가능한 콘서트 회차 목록을 조회한다.")
    @GetMapping("/performance")
    public ApiResponse<List<SelectPerformanceRes>> selectPerformnce(@RequestParam("concertId") long concertId) {
        List<ConcertPerformance> concertPerformanceList = concertUseCase.selectPerformances(concertId);

        List<SelectPerformanceRes> response = concertPerformanceList.stream()
                .map(concertMapper::toPerformanceResponce)
                .toList();
        return ApiResponse.success(response);
    }

    @Operation(summary = "콘서트 회차 좌석 조회 API", description = "예약 가능한 콘서트 회차의 좌석 목록을 조회한다.")
    @GetMapping("/seat")
    public ApiResponse<List<SelectSeatRes>> selectSeat(@RequestParam("performanceId") long performnaceId) {

        List<Seat> seatList = concertUseCase.selectSeats(performnaceId);
        List<SelectSeatRes> response = seatList.stream()
                .map(concertMapper::toSeatResponce)
                .toList();
        return ApiResponse.success(response);
    }

    @Operation(summary = "좌석 예약 요청 API",description = "좌석 예약을 요청한다.")
    @PostMapping("/reserve")
    public ApiResponse<ReservationRes> reserveConcert(
            @RequestHeader("Authorization") String token,
            @RequestBody ReservationReq request) {

        Reservation reservation = concertUseCase.reserveConcert(token, request.performanceId(), request.seatId());

        ReservationRes response = concertMapper.toResrvationResponse(reservation);
        return ApiResponse.success(response);
    }

    @Operation(summary = "결제 요청 API",description = "결제 되면 좌석 변경 및 예약 상태 변환")
    @PostMapping("/payment")
    public ApiResponse<PaymentRes> paymentConcert(
            @RequestHeader("Authorization") String token,
            @RequestBody PaymentReq request) {

        Payment payment = concertUseCase.paymentReservation(token, request.reservationId());
        PaymentRes response = paymentMapper.toPaymentResponse(payment);
        return ApiResponse.success(response);
    }

}



