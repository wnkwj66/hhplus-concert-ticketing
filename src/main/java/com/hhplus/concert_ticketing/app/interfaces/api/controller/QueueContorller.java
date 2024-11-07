package com.hhplus.concert_ticketing.app.interfaces.api.controller;

import com.hhplus.concert_ticketing.app.application.QueueUseCase;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.interfaces.api.common.ApiResponse;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue.CreateQueueReq;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue.CreateQueueRes;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue.SelectQueueRes;
import com.hhplus.concert_ticketing.app.interfaces.api.mapper.QueueMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "대기열 큐 API", description = "콘서트 대기열에 관련된 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/queue")
public class QueueContorller {

    private final QueueUseCase queueUseCase;
    private final QueueMapper queueMapper;

    @Operation(summary = "토큰 발급 API" , description = "대기열 생성 요청")
    @PostMapping("/")
    public ApiResponse<CreateQueueRes> createQueueToken(
            @RequestBody CreateQueueReq request) {

        Queue queue = queueUseCase.enterQueue(request);
        CreateQueueRes response = queueMapper.toCreateQueueRes(queue);
        return ApiResponse.success(response);
    }

    @Operation(summary = "대기열 토큰 체크 API")
    @PostMapping("/check")
    public ApiResponse<SelectQueueRes> getQueueToken(@RequestHeader("Authorization") String tokenId){
        QueueReulst queue = queueUseCase.checkQueue(tokenId);
        SelectQueueRes response = queueMapper.toSelectQueueRes(queue);
        return ApiResponse.success(response);
    }

}
