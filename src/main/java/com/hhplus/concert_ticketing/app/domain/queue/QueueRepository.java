package com.hhplus.concert_ticketing.app.domain.queue;


import org.springframework.data.domain.Pageable;
import java.util.List;

public interface QueueRepository {
    Queue findByToken(String token);

    Queue findQueueInfo(Long userId, Long concertId, Long performanceId);

    void save(Queue queue);

    List<Queue> getQueuesByConcertAndPerformance(Long concertId, Long performanceId, QueueStatus queueStatus);

    Long countByConcertIdAndPerformanceIdAndStatus(Long concertId, Long performanceId, QueueStatus queueStatus);

    void updateExpireConditionToken();

    List<Queue> findWaitingForActivation(Long concertId, Long performanceId, Pageable pageable);
}
