/*
package com.hhplus.concert_ticketing.infra.queue;

import com.hhplus.concert_ticketing.domain.queue.QueueToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface QueueJPARepository {
    Optional<QueueToken> findByUserIdAndConcertId(Long userId, Long concertId);

    @Query("SELECT COUNT(q) FROM QueueToken q WHERE q.concertId = :concertId")
    int countByConcertId(@Param("concertId") Long concertId);

    QueueToken save(QueueToken token);
}
*/
