package com.hhplus.concert_ticketing.domain.concert;

import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.interfaces.api.controller.dto.SelectConcertRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface JpaConcertRepository extends JpaRepository<Concert,Long> {

    @Query("SELECT c FROM Concert c WHERE c.reservationStartAt <= :now and c.startDate >= :now")
    List<Concert> selectConcertList(LocalDateTime now);
}
