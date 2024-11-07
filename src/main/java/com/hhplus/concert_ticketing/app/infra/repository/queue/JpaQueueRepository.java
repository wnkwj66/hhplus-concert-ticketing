package com.hhplus.concert_ticketing.app.infra.repository.queue;

import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaQueueRepository extends JpaRepository<Queue,Long> {
    Optional<Queue> findByTokenId(String tokenId);

    @Query("""
               SELECT q FROM Queue q
               WHERE q.userId =:userId
               AND q.concertId =:concertId
               ANd q.performanceId =:performanceId
               AND (q.status = com.hhplus.concert_ticketing.domain.queue.QueueStatus.WAITING OR q.status = com.hhplus.concert_ticketing.domain.queue.QueueStatus.ACTIVE)
               ORDER BY q.createAt DESC
           """)
    Optional<Queue> findByToken(@Param("userId")Long userId, @Param("concertId")Long concertId, @Param("performanceId")Long performanceId);

    List<Queue> findAllByConcertIdAndPerformanceIdAndStatusOrderByIdDesc(Long concertId, Long performanceId, QueueStatus queueStatus);

    Long countByConcertIdAndPerformanceIdAndStatus(Long concertId, Long performanceId, QueueStatus queueStatus);

    @Modifying
    @Query("""
                 UPDATE Queue q SET q.status = :queueStatus
                 WHERE q.status = ACTIVE AND q.createAt < :conditionExpiredAt
            """)
    void updateStatusExpire(QueueStatus queueStatus, LocalDateTime conditionExpiredAt);

    @Query("""
                SELECT q FROM Queue q
                WHERE q.concertId = :concertId
                AND q.performanceId = :performanceId
                AND q.status = com.hhplus.concert_ticketing.domain.queue.QueueStatus.WAITING
                ORDER BY q.id ASC
            """)
    List<Queue> findWaitingForActivation(@Param("concertId") Long concertId,
                                         @Param("performanceId") Long performanceId,
                                         Pageable pageable);
}
