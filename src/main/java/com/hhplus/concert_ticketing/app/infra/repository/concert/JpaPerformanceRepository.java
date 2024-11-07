package com.hhplus.concert_ticketing.app.infra.repository.concert;

import com.hhplus.concert_ticketing.app.domain.concert.ConcertStatus;
import com.hhplus.concert_ticketing.app.domain.concert.ConcertPerformance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
