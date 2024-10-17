package com.hhplus.concert_ticketing.api.concert.dto;

import java.time.LocalDate;

public record ConcertReqInfo(String concertTitle,
                             String stutus,
                             LocalDate concertStartDate,
                             LocalDate concertEndDate)  {
}
