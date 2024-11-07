package com.hhplus.concert_ticketing.app.application;

import com.hhplus.concert_ticketing.app.domain.concert.ConcertPerformance;
import com.hhplus.concert_ticketing.app.domain.concert.ConcertRepository;
import com.hhplus.concert_ticketing.app.domain.concert.ConcertStatus;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueRepository;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.QueueReulst;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue.CreateQueueReq;
import com.hhplus.concert_ticketing.app.interfaces.api.controller.dto.queue.SelectQueueReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueUseCase {
    private final QueueRepository queueRepository;
    private final ConcertRepository concertRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final int EXPIRED_TIME = 5;

    @Transactional
    public Queue enterQueue(CreateQueueReq request) {
        Queue queue = new Queue(request.userId(), request.concertId(), request.performanceId(), Queue.generateJwtToken(request.userId()));
        String sortedSetKey = "queue:" + request.concertId() + ":" + request.performanceId();
        double score = System.currentTimeMillis();

        redisTemplate.opsForZSet().add(sortedSetKey, queue.getToken(), score);
        // 토큰 만료 시간 설정 (5분)
        redisTemplate.expire(sortedSetKey, EXPIRED_TIME, TimeUnit.MINUTES);

        return queue;
    }

    @Transactional
    public QueueReulst checkQueue(String tokenId , SelectQueueReq request) {
        // Redis에서 대기열 객체를 먼저 조회
        String sortedSetKey = "queueSortedSet:" + request.concertId() + ":" + request.performanceId();
        // 사용자의 순위를 조회
        Long rank = redisTemplate.opsForZSet().rank(sortedSetKey, tokenId);
        log.info("[CreateRedisTokenUseCase] 현재 WorkingQueue 내의 사용자수 : " + rank);
        if (rank != null) {
            // 순위가 존재하면 활성 상태로 간주하고 순위 반환 (0부터 시작하므로 +1 해주기)
            QueueReulst reulst = new QueueReulst(new Queue(request.userId(), request.concertId(), request.performanceId(), tokenId), (rank+1));
            return reulst;
        } else {
            return new QueueReulst(null,null);
        }
    }

    @Transactional
    public void activateQueueForPerformances() {
        // 공연 목록을 가져와 각 공연별로 큐를 처리
        int availableSeat = 0;
        List<ConcertPerformance> performances = concertRepository.findByAvailableSeatGreaterThanOrStatusNot(availableSeat, ConcertStatus.SOLD_OUT);

        for (ConcertPerformance performance : performances) {
            Long concertId = performance.getConcertId();
            Long performanceId = performance.getId();
            String sortedSetKey = "queue:" + concertId + ":" + performanceId;  // 콘서트 및 공연별 대기열 키 생성
            // 대기열에서 앞에 있는 50명의 사용자 조회
            Set<String> activeUsers = redisTemplate.opsForZSet().range(sortedSetKey, 0, 49); // 50명 조회

            for (String tokenId : activeUsers) {
                // 예시: 사용자를 활성화 상태로 이동시키기
                redisTemplate.opsForZSet().remove(sortedSetKey, tokenId);  // 원래 대기열에서 제거
                redisTemplate.opsForZSet().add("activeQueue:" + concertId + ":" + performanceId, tokenId, System.currentTimeMillis());  // 활성화된 대기열에 추가
            }
        }
    }
}
