package com.hhplus.concert_ticketing.app.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "POINT_HISTORY")
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "amount",nullable = false)
    private Integer amount;

    @Column(name = "type",nullable = false)
    private PointType type;

    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

    public PointHistory(Long userId, Integer amount, PointType pointType) {
        this.userId = userId;
        this.amount = amount;
        this.type = pointType;
        this.createAt = LocalDateTime.now();
    };

    public static PointHistory enterPointHitory(Long userId, Integer amount, PointType pointType) {
       return new PointHistory(userId,amount,pointType);
    }
}
