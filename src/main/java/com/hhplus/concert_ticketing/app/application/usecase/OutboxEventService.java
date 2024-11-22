package com.hhplus.concert_ticketing.app.application.usecase;

import com.hhplus.concert_ticketing.app.domain.outbox.OutboxEvent;
import com.hhplus.concert_ticketing.app.domain.outbox.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public void save(OutboxEvent outboxEvent) {
        outboxEventRepository.save(outboxEvent);
    }

    @Transactional(readOnly = true)
    public List<OutboxEvent> getPendingEvents() {
        return outboxEventRepository.findByStatus("INIT");
    }

    @Transactional
    public void markAsPublished(Long id) {
        OutboxEvent event = outboxEventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
        event.markAsPublish();
        outboxEventRepository.save(event);
    }

    @Transactional
    public OutboxEvent findByMessage(String message) {
        return outboxEventRepository.findByEventPayload(message);
    }
}
