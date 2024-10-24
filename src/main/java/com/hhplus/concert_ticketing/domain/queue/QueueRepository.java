package com.hhplus.concert_ticketing.domain.queue;

public interface QueueRepository {
    Queue findByToken(String token);
}
