package com.hhplus.concert_ticketing.api.concert.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ConcertInfo(Long concertId,
                          String concertTitle,
                          String stutus,
                          LocalDateTime reservationDateAt,
                          LocalDate concertStartDate,
                          LocalDate concertEndDate) {
}
