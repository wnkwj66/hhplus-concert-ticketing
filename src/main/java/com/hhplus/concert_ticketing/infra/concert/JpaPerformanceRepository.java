package com.hhplus.concert_ticketing.infra.concert;

import com.hhplus.concert_ticketing.domain.concert.Concert;
import com.hhplus.concert_ticketing.domain.concert.ConcertStatus;
import com.hhplus.concert_ticketing.domain.concert.ConcertPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaPerformanceRepository  extends JpaRepository<ConcertPerformance,Long>{

/*
    @Query("""
               SELECT p FROM Performance p
               WHERE p.concertId =:concertId
               AND p.availableSeat =:availableSeat
               AND p.status = "AVAILABLE"
          """)
    List<ConcertPerformance> findAvailablePerformances(
            @Param("concertId") Long concertId,
            @Param("availableSeat") int availableSeat);
*/


    List<ConcertPerformance> findByAvailableSeatGreaterThanOrStatusNot(int availableSeat, ConcertStatus soldOut);

    List<ConcertPerformance> findByConcertIdAndAvailableSeatGreaterThan(Long concertId, int availableSeat);
}
