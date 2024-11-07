package com.hhplus.concert_ticketing.app.interfaces.api.controller;


import com.hhplus.concert_ticketing.app.application.UserUseCase;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.infra.repository.user.JpaPointRepository;
import com.hhplus.concert_ticketing.app.infra.repository.user.JpaUsersRepository;
import com.hhplus.concert_ticketing.app.interfaces.api.common.ApiResponse;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.user.UserAmountChargeReq;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.user.UserAmountChargeRes;
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
    private final JpaUsersRepository jpaUsersRepository;
    private final JpaPointRepository pointRepository;

    @PostMapping("/add")
    public ApiResponse<String> addUser(@RequestParam("name") String name) {
        Users user = new Users(name);
        Users result = jpaUsersRepository.save(user);

        long userId = jpaUsersRepository.findById(result.getId()).get().getId();

        Point point = new Point(userId,0);
        pointRepository.save(point);

        return ApiResponse.success(Queue.generateJwtToken(userId));

    }

    @Operation(summary = "유저 요금 조회 API")
    @GetMapping("/amount")
    public ApiResponse<UserAmountChargeRes> UserAmountCharge(
            @RequestHeader("Authorization") String token
    ) {
        return ApiResponse.success(new UserAmountChargeRes(userUseCase.selectUserAmount(token)));
    }


    @Operation(summary = "유저 요금 충전 API")
    @PostMapping("/amount/optimistic")
    public ApiResponse<UserAmountChargeRes> selectUserAmountOptimisticLock(
            @RequestHeader("Authorization") String token,
            @RequestBody UserAmountChargeReq request
    ) {
        return ApiResponse.success(new UserAmountChargeRes(userUseCase.chargeUserAmountOptimisticLock(token, request.amount())));
    }
    @Operation(summary = "유저 요금 충전 API")
    @PostMapping("/amount/pessimistic")
    public ApiResponse<UserAmountChargeRes> selectUserAmountPessimisticLock(
            @RequestHeader("Authorization") String token,
            @RequestBody UserAmountChargeReq request
    ) {
        return ApiResponse.success(new UserAmountChargeRes(userUseCase.chargeUserAmountPessimisticLock(token, request.amount())));
    }
    @Operation(summary = "유저 요금 충전 API")
    @PostMapping("/amount/redis")
    public ApiResponse<UserAmountChargeRes> selectUserAmountRedisLock(
            @RequestHeader("Authorization") String token,
            @RequestBody UserAmountChargeReq request
    ) {
        return ApiResponse.success(new UserAmountChargeRes(userUseCase.chargeUserAmountRedisLock(token, request.amount())));
    }
}
