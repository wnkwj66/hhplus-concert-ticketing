package com.hhplus.concert_ticketing.interfaces.api.controller;

import com.hhplus.concert_ticketing.application.ConcertUseCase;
import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.interfaces.api.common.ApiResponse;
import com.hhplus.concert_ticketing.interfaces.api.controller.dto.SelectConcertRes;
import com.hhplus.concert_ticketing.interfaces.api.mapper.ConcertMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "콘서트 API", description = "콘서트 예매와 관련된 API 입니다. 모든 API는 대기열 토큰 헤더(Authorization) 가 필요합니다.")
@RestController // @Controller와 다르게 반환하려는 주류는 JSON 형태 @Controller는 주로 view 반환
@RequiredArgsConstructor // DI(의존성 주입)의 방법 중에 생성자 주입을 임의의 코드없이 자동으로 설정
@RequestMapping("/api/v1/concerts") // REST Api의 버전관리
public class ConcertController {
    private final ConcertUseCase concertUseCase;
    private final ConcertMapper concertMapper;

    @Operation(summary = "콘서트 조회 API", description = "예약 가능한 콘서트 목록을 조회한다.")
    @GetMapping("/")
    public ApiResponse<List<SelectConcertRes>> selectConcert() {
        List<Concert> concertList = concertUseCase.selectConcertList();

        List<SelectConcertRes> reponse = concertList.stream()
                .map(concertMapper::toConcertResponse)
                .toList();

        return ApiResponse.success(reponse);
    }

}



