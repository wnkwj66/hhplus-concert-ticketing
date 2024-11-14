package com.hhplus.concert_ticketing.app.domain.queue;


import java.util.List;
import java.util.Optional;

public interface QueueRepository {

    void save(Queue queue);

    Optional<Queue> findByUserIdAndConcertIdAndScheduleId(Long userId, Long concertId, Long scheduleId);

    Integer findMaxOrderByConcertIdAndScheduleId(Long concertId, Long scheduleId);

    List<Queue> findTopByConcertIdAndScheduleId(Long concertId, Long scheduleId);
}
