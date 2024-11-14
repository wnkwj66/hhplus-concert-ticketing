package com.hhplus.concert_ticketing.app.infra.concert;

import com.hhplus.concert_ticketing.app.domain.concert.ConcertStatus;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<Schedule,Long>{


    List<Schedule> findByAvailableSeatGreaterThanOrStatusNot(int availableSeat, ConcertStatus soldOut);

    List<Schedule> findByConcertIdAndAvailableSeatGreaterThan(Long concertId, int availableSeat);

    List<Schedule> findByConcertId(Long concertId);
}
