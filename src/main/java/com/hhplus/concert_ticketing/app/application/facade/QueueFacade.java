package com.hhplus.concert_ticketing.app.application.facade;

import com.hhplus.concert_ticketing.app.application.usecase.QueueService;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.queue.dto.EnterQueueReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class QueueFacade {
    private final QueueService queueService;

    @Transactional
    public Queue enterQueue(EnterQueueReq request) {
        return queueService.addUserToQueue(request);
    }

    @Transactional
    public void passQueueForSchedule() {
        queueService.passQueueForSchedule();
    }
}
