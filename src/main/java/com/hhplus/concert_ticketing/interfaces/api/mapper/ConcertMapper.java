package com.hhplus.concert_ticketing.interfaces.api.mapper;

import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.interfaces.api.controller.dto.SelectConcertRes;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class ConcertMapper {

    public SelectConcertRes toConcertResponse(Concert concert) {
        return new SelectConcertRes(
                concert.getId(),
                concert.getTitle(),
                concert.getReservationStartAt(),
                concert.getStartDate(),
                concert.getEndDate()
        );
    }
}
