package com.hhplus.concert_ticketing.app.application;

import com.hhplus.concert_ticketing.app.domain.concert.ConcertPerformance;
import com.hhplus.concert_ticketing.app.domain.concert.ConcertRepository;
import com.hhplus.concert_ticketing.app.domain.concert.ConcertStatus;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueRepository;
import com.hhplus.concert_ticketing.app.domain.queue.QueueStatus;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.QueueReulst;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue.CreateQueueReq;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueUseCase {
    private final QueueRepository queueRepository;
    private final ConcertRepository concertRepository;

    @Transactional
    public Queue enterQueue(CreateQueueReq request) {
        Queue verifyQueue = queueRepository.findQueueInfo(request.userId(), request.performanceId() ,request.concertId());

        // 엔티티 체크 (유효성 검증에서 실패시 새로운 객체(토큰) 반환)
        Queue queue = Queue.enterQueue(verifyQueue, request.userId(),request.concertId(),request.performanceId());

        // 변경되지 않은 엔티티는 업데이트 되지 않음.
        queueRepository.save(queue);
        return queue;
    }

    @Transactional
    public QueueReulst checkQueue(String tokenId) {
        Queue queue = queueRepository.findByToken(tokenId);
        Claims claims = Users.parseJwtToken(tokenId);

        Long concertId = claims.get("concertId", Long.class);
        Long performanceId = claims.get("performanceId", Long.class);

//        List<Queue> watingQueueList = queueRepository.getQueuesByConcertAndPerformance(concertId,performanceId,QueueStatus.WAITING);
         Long waitingCount = queueRepository.countByConcertIdAndPerformanceIdAndStatus(concertId, performanceId, QueueStatus.WAITING)+1;

         return new QueueReulst(queue,waitingCount);
    }
    /**
     * 토큰 만료 여부 조건일 경우 토큰 상태값을 업데이트 한다.
     */
    @Transactional
    public void updateExpireConditionToken() {
        queueRepository.updateExpireConditionToken();
    }

    @Transactional
    public void activateQueueForPerformances() {
        // 공연 목록을 가져와 각 공연별로 큐를 처리
        int availableSeat = 0;
        List<ConcertPerformance> performances = concertRepository.findByAvailableSeatGreaterThanOrStatusNot(availableSeat, ConcertStatus.SOLD_OUT);

        for (ConcertPerformance performance : performances) {
            Long concertId = performance.getConcertId();
            Long performanceId = performance.getId();
            activateMUsersInQueue(concertId, performanceId, QueueStatus.WAITING, 5); // 예: M=5
        }
    }

    public void activateMUsersInQueue(Long concertId, Long performanceId, QueueStatus status, int m) {
        // 대기 중인 M명 조회 (대기열에서 대기 중인 사용자 내림차순 정렬)
        PageRequest pageable = PageRequest.of(0, m);  // m개의 결과를 가져옴
        List<Queue> waitingQueues = queueRepository.findWaitingForActivation(concertId, performanceId, (Pageable) pageable);

        // M명 활성화 (대기 상태에서 다음 단계로 상태 전환)
        waitingQueues.forEach(queue -> {
            queue.expire();
            queueRepository.save(queue);
        });
    }
}
