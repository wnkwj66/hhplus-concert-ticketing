package com.hhplus.concert_ticketing.interfaces.api.controller;

import com.hhplus.concert_ticketing.domain.queue.Queue;

public record QueueReulst(
        Queue queue,
        Long position
) {
}
