package com.hhplus.concert_ticketing.app.domain.concert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ConcertRepository {

    Concert findById(Long concertId);

    Concert save(Concert concert);

    List<Concert> getConcertList(LocalDateTime now , LocalDate now1);

}
