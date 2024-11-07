package com.hhplus.concert_ticketing.app.interfaces.scheduler;


import com.hhplus.concert_ticketing.app.application.QueueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueScheduler {

    private final QueueUseCase queueUseCase;

//    @Scheduled(fixedDelay = 5000, initialDelay = 1800_000)
//    public void checkTokenExpire() {
//        queueUseCase.updateExpireConditionToken();
//    }

    @Scheduled(fixedDelay = 5000)
    public void processQueue() {
        queueUseCase.activateQueueForPerformances();
    }
}
