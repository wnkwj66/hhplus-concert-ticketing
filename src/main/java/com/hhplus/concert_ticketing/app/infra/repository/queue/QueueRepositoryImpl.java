package com.hhplus.concert_ticketing.app.infra.repository.queue;

import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueRepository;
import com.hhplus.concert_ticketing.app.domain.queue.QueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {
    private final JpaQueueRepository jpaQueueRepository;

    @Override
    public Queue findByToken(String tokenId) {
        return jpaQueueRepository.findByTokenId(tokenId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    @Override
    public Queue findQueueInfo(Long userId, Long concertId, Long performanceId) {
        return jpaQueueRepository.findByToken(userId, concertId, performanceId).orElse(null);
    }

    @Override
    public void save(Queue queue) {
        jpaQueueRepository.save(queue);
    }

    @Override
    public List<Queue> getQueuesByConcertAndPerformance(Long concertId, Long performanceId, QueueStatus queueStatus) {
        return jpaQueueRepository.findAllByConcertIdAndPerformanceIdAndStatusOrderByIdDesc(concertId,performanceId,queueStatus);
    }

    @Override
    public Long countByConcertIdAndPerformanceIdAndStatus(Long concertId, Long performanceId, QueueStatus queueStatus) {
        return jpaQueueRepository.countByConcertIdAndPerformanceIdAndStatus(concertId,performanceId,queueStatus);
    }

    @Override
    public void updateExpireConditionToken() {
        jpaQueueRepository.updateStatusExpire(
                QueueStatus.EXPIRED,
                LocalDateTime.now()
        );
    }

    @Override
    public List<Queue> findWaitingForActivation(Long concertId, Long performanceId, Pageable pageable) {
        return jpaQueueRepository.findWaitingForActivation(concertId,performanceId,pageable);
    }


}
