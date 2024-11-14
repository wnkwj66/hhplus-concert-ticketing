package com.hhplus.concert_ticketing.app.domain.queue;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.*;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "QUEUE")
public class Queue {
    private static final String SECRET_KEY = Base64.getEncoder().encodeToString("7v3Y8xD3k6W1rTqH4z5NqD7s6Jv2F8x3".getBytes());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "concert_id",nullable = false)
    private Long concertId;

    @Column(name = "schedule_id",nullable = false)
    private Long scheduleId;

    @Column(name = "queue_order")
    private Integer queueOrder;

    @Column(name = "token")
    private String token;

    @Column(name = "status")
    private QueueStatus status;

    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;


    public Queue(Long userId, Long concertId, Long scheduleId, Integer queueOrder) {
        this.userId = userId;
        this.concertId = concertId;
        this.scheduleId = scheduleId;
        this.queueOrder = queueOrder;
        this.status = QueueStatus.WAITING;
        this.createAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }

    // JWT 토큰 생성
    public static String generateJwtToken(Long userId) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("token", UUID.randomUUID().toString())
                .claim("createAt", new Date())
                .claim("expiredAt", new Date(System.currentTimeMillis() + 300000)) // 5분 후 만료
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Base64 인코딩된 시크릿 키 사용
                .compact();
    }

    // 대기열 상태 검증 메서드
    public boolean isInQueue() {
        return QueueStatus.WAITING.equals(status) && LocalDateTime.now().isBefore(expiredAt);
    }

    public boolean isPassed() {
        return QueueStatus.PASSED.equals(status);
    }

    public boolean isExpired() {
        return QueueStatus.EXPIRED.equals(status) || LocalDateTime.now().isAfter(expiredAt);
    }

}
