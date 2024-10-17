package com.hhplus.concert_ticketing.api.concert.controller;

import com.hhplus.concert_ticketing.api.concert.dto.ConcertInfo;
import com.hhplus.concert_ticketing.api.concert.dto.PerformanceResDto;
import com.hhplus.concert_ticketing.application.concert.ConcertService;
import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.domain.concert.Seat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/concert")
public class ConcertController {
    private final ConcertService concertService;

    public ConcertController(ConcertService concertService) {
        this.concertService = concertService;
    }


    @PostMapping("/test")
    public void saveConcert() {
        Concert concert = new Concert("테스트","예약가능",LocalDateTime.now().minusWeeks(1),LocalDate.now(),LocalDate.now().plusDays(5));
        concertService.save(concert);

    }


    @GetMapping("/")
    public ResponseEntity<List<ConcertInfo>> getConcertList() {

        List<ConcertInfo> concertList = concertService.findConcertList();

        return ResponseEntity.ok(concertList);
    }
    // 콘서트 선택 > 콘서트 조회 (공연시작일,종료일,예매시작일시...)  > 공연날짜 조회 > 공연날짜 선택 > (예매시작일이 지났다면)해당 날짜 좌석조회 API

    @GetMapping("/{concertId}")
    public ResponseEntity<PerformanceResDto> getPerformances(@PathVariable("concertId") Long concertId) {
        // 콘서트 선택 후 예약 가능 날짜 조회
        // 콘서트 선택 > 콘서트 조회 (공연시작일,종료일,예매시작일시)
        PerformanceResDto concertInfo = concertService.findPerformances(concertId);

        return ResponseEntity.ok(concertInfo);
    }


    @GetMapping("/{concertId}/{perpormanceId}/seat")
    public ResponseEntity<List<Seat>> getSeats(@PathVariable Long concertId,
                                         @PathVariable Long performanceId) {

        List<Seat> seatList = concertService.getAvailableSeatList(concertId,performanceId);

        return ResponseEntity.ok(seatList);
    }

}








