package com.hhplus.concert_ticketing.app.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id" ,nullable = false)
    private Long userId;

    @Column(name = "amount",nullable = false)
    private Integer amount;

    @Version
    private Integer version;

    public Point(Long userId, Integer amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public void chargePoint(Integer amount) {
        if(0 >= amount) {
          throw new IllegalArgumentException("충전금액이 없습니다.");
        }
        this.amount += amount;
    }

    public void addAmount(Integer amount) {
        if(0 >= amount) {
            throw new IllegalArgumentException("충전금액을 0 이상으로 설정해주세요.");
        }

        this.amount += amount;
    }
}
