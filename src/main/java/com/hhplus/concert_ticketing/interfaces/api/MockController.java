package com.hhplus.concert_ticketing.interfaces.api;

import com.hhplus.concert_ticketing.interfaces.dto.concert.*;
import com.hhplus.concert_ticketing.interfaces.dto.payment.*;
import com.hhplus.concert_ticketing.interfaces.dto.queue.TokenActiveResDto;
import com.hhplus.concert_ticketing.interfaces.dto.queue.TokenReqDto;
import com.hhplus.concert_ticketing.interfaces.dto.queue.TokenResDto;
import com.hhplus.concert_ticketing.interfaces.dto.queue.TokenStatusResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mock/api")
public class MockController {
    private static final int EXPIREDTIME = 5;

    @Operation(summary = "대기열 토큰 발급 요청 API", description = "토큰을 발급받습니다.")
    @PostMapping("/tokens")
    public ResponseEntity<TokenResDto> getToken(@RequestBody TokenReqDto request) {
        return  ResponseEntity.status(HttpStatus.CREATED).body(new TokenResDto(
            1L,
                request.userId(),
                request.concertId(),
                "WAIT",
                1,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(EXPIREDTIME)
        ));
    }

    @Operation(summary = "대기열 상태 확인 API", description = "대기열에 등록된 토큰 상태를 조회합니다.")
    @GetMapping("/tokens/status")
    public ResponseEntity<TokenStatusResDto> getTokenStatus(@PathVariable Long tokenId) {
        return ResponseEntity.ok(new TokenStatusResDto(
                1L,
                1L,
                "WAIT"
        ));
    }
    @Operation(summary = "토큰 활성화 API", description = "대기중인 토큰을 활성화합니다.")
    @PatchMapping("/{tokenId}")
    public ResponseEntity<TokenActiveResDto> activeTokens(@PathVariable Long tokenId) {
        return ResponseEntity.ok(new TokenActiveResDto(
                1L,
                1L,
                1L,
                "ACTIVE",
                LocalDateTime.now()
        ));
    }

    @Operation(summary = "토큰 검증 API", description = "토큰이 발급되었는지 검증합니다.")
    @RequestMapping(value = "/{tokenId}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> validateToken(@PathVariable Long tokenId) {
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "예약 가능날짜 조회 API", description = "예약 가능한 날짜를 조회합니다.")
    @GetMapping("/{concertId}/performance")
    public ResponseEntity<PerformanceResDto> getPerformances(@PathVariable Long concertId,
                                                             @RequestHeader("Authorization") Long tokenId) {
        return ResponseEntity.ok(new PerformanceResDto(
                concertId,
                "xxx 콘서트",
                "예약가능",
                List.of(new Performance(LocalDateTime.of(2024,10,26,17,30)))
        ));
    }

    @Operation(summary = "좌석 조회 API", description = "선택한 콘서트 공연의 예약 가능한 좌석을 조회합니다.")
    @GetMapping("/{concertId}/{performanceId}/seat")
    public ResponseEntity<SeatResDto> getSeats(@PathVariable Long concertId,
                                               @PathVariable Long performanceId,
                                               @RequestHeader("Authorization") Long tokenId) {
        return ResponseEntity.ok(new SeatResDto(
                concertId,
                "xxx 콘서트",
                performanceId,
                LocalDateTime.of(2024,10,26,17,30),
                List.of(new Seat(1L,1,"AVAILABLE",30000),
                        new Seat(15L,15,"AVAILABLE",20000),
                        new Seat(21L,21,"AVAILABLE",20000))
        ));
    }

    @Operation(summary = "좌석 예약 요청 API", description = "선택한 콘서트 공연의 좌석 예약을 요청합니다.")
    @PostMapping("/{concertId}/{performaceId}/reservation")
    public ResponseEntity<ReservaitonResDto> reserveation(@PathVariable Long concertId,
                                                          @PathVariable Long performanceId,
                                                          @RequestHeader("Authorization") Long tokenId,
                                                          @RequestBody ReservationReqDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservaitonResDto(
                1L,
                request.userId(),
                concertId,
                "TEMPORARY",
                LocalDateTime.now(),
                request.performanceAt(),
                request.seatNumber()
        ));
    }

    @Operation(summary = "잔액조회 API", description = "사용자가 잔액을 조회합니다.")
    @GetMapping("/users/{userId}/point")
    public ResponseEntity<PointResDto> getPoint(@PathVariable Long userId) {
        return ResponseEntity.ok(new PointResDto(
                userId,
                10000
        ));
    }

    @Operation(summary = "잔액충전 API", description = "사용자가 잔액을 충전합니다.")
    @PostMapping("/users/{userId}/point")
    public ResponseEntity<PointChargeResDto> chargePoint(@PathVariable Long userId, @RequestBody PointReqDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new PointChargeResDto(
                userId,
                request.amount(),
                LocalDateTime.now()
        ));
    }

    @Operation(summary = "결제 API", description = "사용자가 예매한 내역을 결제합니다.")
    @PostMapping("/payments")
    public ResponseEntity<PaymentResDto> paymentProcess(@RequestBody PaymentReqDto request,
                                                        @RequestHeader("Authorization") Long tokenId){
        return  ResponseEntity.status(HttpStatus.CREATED).body(new PaymentResDto(
                request.userId(),
                20000,
                LocalDateTime.now()
        ));
    }


}
