package com.hhplus.concert_ticketing.app.domain.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    // PENDING 상태의 이벤트 조회
    List<OutboxEvent> findByStatus(String status);
}
