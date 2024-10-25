package com.hhplus.concert_ticketing.infra.concert;

import com.hhplus.concert_ticketing.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface JpaConcertRepository extends JpaRepository<Concert,Long> {

    @Query("SELECT c FROM Concert c WHERE c.reservationStartAt <= :now and c.endDate >= :now1")
    List<Concert> selectConcertList(LocalDateTime now ,LocalDate now1);
}
