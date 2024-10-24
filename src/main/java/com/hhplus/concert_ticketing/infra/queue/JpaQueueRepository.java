package com.hhplus.concert_ticketing.infra.queue;

import com.hhplus.concert_ticketing.domain.queue.Queue;
import com.hhplus.concert_ticketing.domain.queue.QueueStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaQueueRepository {
    Optional<Queue> findByToken(String token);

    @Query("""
               SELECT q FROM Queue q
               WHERE q.userId =:userId
               AND q.concertId =:concertId
               ANd q.performanceId =:performanceId
               AND (q.status = "WAITING" OR q.status = "ACTIVE")
               ORDER BY q.createAt DESC
               LIMIT 1
           """)
    Optional<Queue> findByToken(Long userId, Long concertId, Long performanceId);

    void save(Queue queue);

    List<Queue> findAllByConcertIdAndPerformanceIdAndStatusOrderByIdDesc(Long concertId, Long performanceId, QueueStatus queueStatus);

    Long countByConcertIdAndPerformanceIdAndStatus(Long concertId, Long performanceId, QueueStatus queueStatus);

    @Modifying
    @Query("""
                 UPDATE Queue q SET q.status = :queueStatus
                 WHERE q.status = ACTIVE AND q.createAt < :conditionExpiredAt
            """)
    void updateStatusExpire(QueueStatus queueStatus, LocalDateTime conditionExpiredAt);

    List<Queue> findTopMByConcertIdAndPerformanceIdAndStatusOrderByIdAsc(Long concertId, Long performanceId, QueueStatus status, int m);
}
