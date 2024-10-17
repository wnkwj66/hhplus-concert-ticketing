package com.hhplus.concert_ticketing.application.queue;

import com.hhplus.concert_ticketing.api.queue.dto.TokenReqDto;
import com.hhplus.concert_ticketing.domain.queue.QueueToken;

public interface QueueService {
    QueueToken getOrCreateToken(TokenReqDto request);
}
