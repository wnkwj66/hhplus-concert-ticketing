package com.hhplus.concert_ticketing.api.concert.dto;


import com.hhplus.concert_ticketing.domain.concert.Performance;

import java.util.List;

public record PerformanceResDto(
        Long concertId,
        String concertTitle,
        String concertStauts,
        List<Performance> performanceList   // 공연날짜별 회차가 복수개일수 있음
) {
}
