package com.hhplus.concert_ticketing.app.interfaces.scheduler;

import com.hhplus.concert_ticketing.app.application.facade.QueueFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final QueueFacade queueFacade;
    @Scheduled(fixedDelay = 1000) // N초마다 실행
    public void processConcertQueues() {
//        queueFacade.passQueueForSchedule();
    }

}
