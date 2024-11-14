package com.hhplus.concert_ticketing.app.application.usecase;

import com.hhplus.concert_ticketing.app.domain.concert.Concert;
import com.hhplus.concert_ticketing.app.domain.concert.Schedule;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueRepository;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.queue.dto.EnterQueueReq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final ConcertService concertService;
    private final ScheduleService scheduleService;

    private final QueueRepository queueRepository;

    public Queue addUserToQueue(EnterQueueReq request) {
        // 대기열 정보가 있으면 대기열 정보 반환하고, 없으면 새로 추가
        Queue queue = queueRepository.findByUserIdAndConcertIdAndScheduleId(request.userId(), request.concertId(), request.scheduleId())
                .orElseGet(() -> {
                    Queue newQueue = new Queue(request.userId(), request.concertId(), request.scheduleId(), getNextQueueOrder(request.concertId(), request.scheduleId()));
                    queueRepository.save(newQueue);
                    return newQueue;
                });
        if (queue.isExpired()) {
            throw new IllegalStateException("대기열 토큰이 만료되었습니다.");
        }

        if (queue.isPassed()) {
            throw new IllegalStateException("이미 대기열을 통과했습니다.");
        }

        return queue;
    }

    // 해당 콘서트와 회차에 대한 대기열에서 가장 큰 순번을 조회
    private int getNextQueueOrder(Long concertId, Long scheduleId) {
        Integer nextQueueOrder = queueRepository.findMaxOrderByConcertIdAndScheduleId(concertId, scheduleId);

        if (nextQueueOrder == null) {
            return 1;
        }
        return nextQueueOrder + 1;
    }

    // 대기열에서 N명의 유저를 진입시킨다.
    public void passQueueForSchedule() {
        List<Concert> concertList = concertService.getConcertList(LocalDateTime.now(), LocalDate.now());
        concertList.forEach(concert -> {
            List<Schedule> scheduleList = scheduleService.getConcertSchedules(concert.getId());
            scheduleList.forEach(schedule -> {
                userQueueStateActive(concert.getId(), schedule.getId());
            });
        });
    }

    private void userQueueStateActive(Long concertId, Long scheduleId) {
        List<Queue> queueList = queueRepository.findTopByConcertIdAndScheduleId(concertId, scheduleId);
        queueList.forEach(user -> {

        });
    }

    //TODO: 토큰 만료 조건일때 토큰 상태 업데이트 (스케줄러)


}
