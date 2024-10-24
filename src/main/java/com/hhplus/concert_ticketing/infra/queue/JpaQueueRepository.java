package com.hhplus.concert_ticketing.infra.queue;

import com.hhplus.concert_ticketing.domain.queue.Queue;

import java.util.Optional;

public interface JpaQueueRepository {
    Optional<Queue> findByToken(String token);
}
