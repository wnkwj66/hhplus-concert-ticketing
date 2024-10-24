package com.hhplus.concert_ticketing.domain.queue;

import com.hhplus.concert_ticketing.interfaces.api.controller.dto.queue.CreateQueueReq;

import java.util.List;

public interface QueueRepository {
    Queue findByToken(String token);

    Queue findQueueInfo(Long userId, Long concertId, Long performanceId);

    void save(Queue queue);

    List<Queue> getQueuesByConcertAndPerformance(Long concertId, Long performanceId, QueueStatus queueStatus);

    Long countByConcertIdAndPerformanceIdAndStatus(Long concertId, Long performanceId, QueueStatus queueStatus);

    void updateExpireConditionToken();

    List<Queue> findTopMByConcertIdAndPerformanceIdAndStatusOrderByIdAsc(Long concertId, Long performanceId, QueueStatus status, int m);
}
