package com.hhplus.concert_ticketing.domain.queue;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
public class QueueToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = true)
    private Long concertId;

    @Column(nullable = false)
    private String tokenUUID;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    public QueueToken(Long userId, Long concertId, String tokenUUID, String status, int position, LocalDateTime createAt, LocalDateTime updateAt, LocalDateTime expiredAt) {
        this.userId = userId;
        this.concertId = concertId;
        this.tokenUUID = tokenUUID;
        this.status = status;
        this.position = position;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.expiredAt = expiredAt;
    }


    public static QueueToken newQueueToken(Long userId, Long concertId, int position) {

        return new QueueToken(
                userId,
                concertId,
                UUID.randomUUID().toString(),
                "WAIT",
                position,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30)
                );
    }
}
