package com.hhplus.concert_ticketing.app.interfaces.api.controller;

import com.hhplus.concert_ticketing.app.domain.queue.Queue;

public record QueueReulst(
        Queue queue,
        Long position
) {
}
