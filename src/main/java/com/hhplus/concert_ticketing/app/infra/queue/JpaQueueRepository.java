package com.hhplus.concert_ticketing.app.infra.queue;

import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaQueueRepository extends JpaRepository<Queue,Long> {

    Optional<Queue> findByUserIdAndConcertIdAndScheduleId(Long userId, Long concertId, Long scheduleId);

    Integer findMaxOrderByConcertIdAndScheduleId(Long concertId, Long scheduleId);

    List<Queue> findTopByConcertIdAndScheduleId(Long concertId, Long scheduleId);
}
