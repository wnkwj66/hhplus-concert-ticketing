package com.hhplus.concert_ticketing.interfaces.api.controller;


import com.hhplus.concert_ticketing.application.UserUseCase;
import com.hhplus.concert_ticketing.interfaces.api.common.ApiResponse;
import com.hhplus.concert_ticketing.interfaces.api.controller.dto.user.UserAmountChargeReq;
import com.hhplus.concert_ticketing.interfaces.api.controller.dto.user.UserAmountChargeRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "유저 API", description = "유저와 관련된 API 입니다. 모든 API는 대기열 토큰 헤더(Authorization) 가 필요합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/user")
public class UserController {

    private final UserUseCase userUseCase;

    @Operation(summary = "유저 요금 조회 API")
    @GetMapping("/amount")
    public ApiResponse<UserAmountChargeRes> UserAmountCharge(
            @RequestHeader("Authorization") String token
    ) {
        return ApiResponse.success(new UserAmountChargeRes(userUseCase.selectUserAmount(token)));
    }
    @Operation(summary = "유저 요금 충전 API")
    @PostMapping("/amount")

    public ApiResponse<UserAmountChargeRes> selectUserAmount(
            @RequestHeader("Authorization") String token,
            @RequestBody UserAmountChargeReq request
    ) {
        return ApiResponse.success(new UserAmountChargeRes(userUseCase.chargeUserAmount(token, request.amount())));
    }
}
