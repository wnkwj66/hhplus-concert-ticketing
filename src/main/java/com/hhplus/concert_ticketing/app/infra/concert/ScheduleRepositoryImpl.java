package com.hhplus.concert_ticketing.app.infra.concert;

import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import com.hhplus.concert_ticketing.app.domain.concert.ScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@NoRepositoryBean
public class ScheduleRepositoryImpl implements ScheduleRepository {
    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public Schedule findById(Long scheduleId) {
        return scheduleJpaRepository.findById(scheduleId).orElseThrow(() -> new EntityNotFoundException("Concert Schedule not found"));
    }

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleJpaRepository.save(schedule);
    }

    @Override
    public List<Schedule> findByConcertId(Long concertId) {
        return scheduleJpaRepository.findByConcertId(concertId);
    }
}
