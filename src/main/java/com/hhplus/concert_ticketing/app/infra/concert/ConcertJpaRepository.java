package com.hhplus.concert_ticketing.app.infra.concert;

import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConcertJpaRepository extends JpaRepository<Concert,Long> {

    @Query("SELECT c FROM Concert c WHERE c.reservationStartAt <= :now and c.endDate >= :now1")
    List<Concert> getConcertList(LocalDateTime now, LocalDate now1);

}
