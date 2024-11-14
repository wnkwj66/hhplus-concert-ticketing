package com.hhplus.concert_ticketing.app.domain.concert;

import java.util.List;

public interface ScheduleRepository {
    Schedule findById(Long scheduleId);

    Schedule save(Schedule schedule);

    List<Schedule> findByConcertId(Long concertId);
}
