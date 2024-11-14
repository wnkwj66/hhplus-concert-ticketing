package com.hhplus.concert_ticketing.app.interfaces.api.controller.concert;

import com.hhplus.concert_ticketing.app.application.facade.ConcertFacade;
import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import com.hhplus.concert_ticketing.app.domain.seat.Seat;
import com.hhplus.concert_ticketing.app.interfaces.api.common.ApiResponse;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.concert.dto.ConcertRes;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.concert.dto.ScheduleRes;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.concert.dto.SeatRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "콘서트 API", description = "콘서트와 관련된 API 입니다.")
@RestController // @Controller와 다르게 반환하려는 주류는 JSON 형태 @Controller는 주로 view 반환
@RequiredArgsConstructor // DI(의존성 주입)의 방법 중에 생성자 주입을 임의의 코드없이 자동으로 설정
@RequestMapping("/api/v1/concerts") // REST Api의 버전관리
public class ConcertController {
    private final ConcertFacade concertFacade;

    @Operation(summary = "콘서트 조회 API", description = "예약 가능한 콘서트 목록을 조회한다.")
    @GetMapping
    public ApiResponse<List<ConcertRes>> getConcerts() {
        List<Concert> concertList = concertFacade.getConcertList();

        return ApiResponse.success(ConcertRes.of(concertList));
    }

    @Operation(summary = "콘서트 일정 조회 API",description = "해당 콘서트의 회차정보를 조회한다.")
    @GetMapping("/{concertId}/schedules")
    public ApiResponse<List<ScheduleRes>> getSchedules(@PathVariable Long concertId) {
        List<Schedule> scheduleList = concertFacade.getScheduleList(concertId);

        return ApiResponse.success(ScheduleRes.of(scheduleList));
    }

    @Operation(summary = "좌석 조회 API", description = "해당 콘서트 회차의 잔여좌석을 조회한다.")
    @PostMapping("/{concertId}/schedules/{scheduleId}/seats")
    public ApiResponse<List<SeatRes>> getSeats(@PathVariable Long concertId, @PathVariable Long scheduleId) {
        List<Seat> seatList = concertFacade.getSeatList(concertId, scheduleId);

        return ApiResponse.success(SeatRes.of(seatList));
    }

}



