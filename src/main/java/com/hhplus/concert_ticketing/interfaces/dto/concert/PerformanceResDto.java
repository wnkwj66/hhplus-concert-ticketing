package com.hhplus.concert_ticketing.interfaces.dto.concert;

import java.util.List;

public record PerformanceResDto(
        Long concertId,
        String concertName,
        String concertStauts,
        List<Performance> performanceList   // 공연날짜별 회차가 복수개일수 있음
) {
}
