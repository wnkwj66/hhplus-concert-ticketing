package com.hhplus.concert_ticketing.infra.queue;

import com.hhplus.concert_ticketing.domain.queue.QueueToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QueueRepository  extends JpaRepository<QueueToken, Long> {
    Optional<QueueToken> findByUserIdAndConcertId(Long userId, Long concertId);
    Optional<QueueToken> findByUuid(String UUID);

    @Query("SELECT COUNT(q) FROM QueueToken q WHERE q.concertId = :concertId")
    int countByConcertId(@Param("concertId") Long concertId);

    QueueToken save(QueueToken token);
}
