package com.hhplus.concert_ticketing.app.domain.outbox;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "OUTBOX_EVENT")
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    private String eventType; // 이벤트 타입

    @Lob
    @Column(name = "event_payload", nullable = false)
    private String eventPayload; // 이벤트 내용 (JSON 형태)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OutboxEventStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 생성 시간

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정 시간

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now(); // 업데이트 시간 자동 갱신
    }

    // 생성자
    public OutboxEvent(String eventType, String eventPayload) {
        this.eventType = eventType;
        this.eventPayload = eventPayload;
        this.status = OutboxEventStatus.INIT;
    }

    // 상태 변경 메서드
    public void markAsPublish() {
        this.status = OutboxEventStatus.PUBLISH;
    }

}
