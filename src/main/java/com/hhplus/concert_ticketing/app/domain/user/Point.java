package com.hhplus.concert_ticketing.app.domain.user;

import com.hhplus.concert_ticketing.app.interfaces.exception.ApiException;
import com.hhplus.concert_ticketing.app.interfaces.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.logging.LogLevel;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount",nullable = false)
    private Integer amount;

    public Point(Integer amount) {
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

    public void deductAmount(Integer amount){
        if(0 >= amount) {
            throw new IllegalArgumentException("사용금액은 0보다 작을수 없습니다.");
        }
        if (this.amount < amount) {
            throw new ApiException(ErrorCode.INSUFFICIENT_POINT_ERROR, LogLevel.INFO);
        }
        this.amount -= amount;
    }
}
