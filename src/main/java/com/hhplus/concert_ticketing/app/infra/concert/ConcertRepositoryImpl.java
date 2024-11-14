package com.hhplus.concert_ticketing.app.infra.concert;

import com.hhplus.concert_ticketing.app.domain.concert.*;
import com.hhplus.concert_ticketing.app.domain.seat.Seat;
import com.hhplus.concert_ticketing.app.infra.seat.SeatJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@NoRepositoryBean
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ScheduleJpaRepository scheduleJpaRepository;
    private final SeatJpaRepository seatJpaRepository;

    @Override
    public Concert findById(Long id) {
        return concertJpaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Concert not found"));
    }

    @Override
    public Concert save(Concert concert) {
        concertJpaRepository.save(concert);
        return concert;
    }

    @Override
    public List<Concert> getConcertList(LocalDateTime now, LocalDate now1) {
        return concertJpaRepository.getConcertList(now,now1);
    }


}
