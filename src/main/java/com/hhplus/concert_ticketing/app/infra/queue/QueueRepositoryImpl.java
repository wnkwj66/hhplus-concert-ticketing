package com.hhplus.concert_ticketing.app.infra.queue;

import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueRepository;
import com.hhplus.concert_ticketing.app.domain.queue.QueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {
    private final JpaQueueRepository jpaQueueRepository;

    @Override
    public void save(Queue queue) {
        jpaQueueRepository.save(queue);
    }

    @Override
    public Optional<Queue> findByUserIdAndConcertIdAndScheduleId(Long userId, Long concertId, Long scheduleId) {
        return jpaQueueRepository.findByUserIdAndConcertIdAndScheduleId(userId,concertId,scheduleId);
    }

    @Override
    public Integer findMaxOrderByConcertIdAndScheduleId(Long concertId, Long scheduleId) {
        return jpaQueueRepository.findMaxOrderByConcertIdAndScheduleId(concertId, scheduleId);
    }

    @Override
    public List<Queue> findTopByConcertIdAndScheduleId(Long concertId, Long scheduleId) {
        return jpaQueueRepository.findTopByConcertIdAndScheduleId(concertId,scheduleId);
    }

}
