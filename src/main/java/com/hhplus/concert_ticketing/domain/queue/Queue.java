package com.hhplus.concert_ticketing.domain.queue;

import com.hhplus.concert_ticketing.interfaces.exception.ApiException;
import com.hhplus.concert_ticketing.interfaces.exception.ErrorCode;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.*;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.logging.LogLevel;

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

    @Column(name = "performance_id",nullable = false)
    private Long performanceId;

    @Column(name = "token_id",nullable = false)
    private String tokenId;

    @Column(name = "status",nullable = false)
    private QueueStatus status;

    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at",nullable = false)
    private LocalDateTime updateAt;

    @Column(name = "expired_at",nullable = false)
    private LocalDateTime expiredAt;

    public Queue(Long userId, Long concertId, Long performanceId, String tokenId, QueueStatus status) {
        this.userId = userId;
        this.concertId = concertId;
        this.performanceId = performanceId;
        this.tokenId = tokenId;
        this.status = status;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }

    public Queue(Long userId, Long concertId, Long performanceId, String tokenId) {
        this.userId = userId;
        this.concertId = concertId;
        this.performanceId = performanceId;
        this.tokenId = tokenId;
        this.status = QueueStatus.WAITING;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }

    // 새 토큰을 발급하거나 기존 토큰 반환
    public static Queue enterQueue(Queue verifyQueue , Long userId, Long concertId, Long performanceId) {
        if(verifyQueue != null) {
            return verifyQueue;
        }
        return new Queue(userId,concertId,performanceId,generateJwtToken(userId,concertId,performanceId)); // 새 토큰 발급
    }

    // JWT 토큰 생성
    public static String generateJwtToken(Long userId, Long concertId, Long performanceId) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("concertId", concertId)
                .claim("performanceId", performanceId)
                .claim("token", UUID.randomUUID().toString())
                .claim("createAt", new Date())
                .claim("expiredAt", new Date(System.currentTimeMillis() + 300000)) // 5분 후 만료
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Base64 인코딩된 시크릿 키 사용
                .compact();
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

    public static int waitingNumber(Queue verifyQueue){
        // 마지막 maxCount 정보 조회 후 반환 (이거 도메인에서 해야함 ?) or 서비스?
        return 1;
    }

    public boolean isValidateToken() {
        // 토큰 상태가 WAITING 또는 ACTIVE 때 유효
        if((status == QueueStatus.WAITING || status == QueueStatus.ACTIVE) &&
                (expiredAt == null || expiredAt.isAfter(LocalDateTime.now()))) {
            // 만료 시간이 없거나, 만료 시간이 현재 시간 이후일 때 유효
            return true;
        }
        return false; // 그 외의 경우 토큰은 유효하지 않음
    }

    public void isValidCheck() {
        if(this.status != QueueStatus.ACTIVE) {
            throw new ApiException(ErrorCode.QUEUE_TOKEN_INVALID_ERROR, LogLevel.INFO);
        }
    }
    public void finishQueue(){
        this.status = QueueStatus.DONE;
    }

    public boolean isExpired() {
        return expiredAt != null && LocalDateTime.now().isAfter(expiredAt);
    }

    public void expire() {
        if (isExpired()) {
            this.status = QueueStatus.EXPIRED;
        }
    }
}
