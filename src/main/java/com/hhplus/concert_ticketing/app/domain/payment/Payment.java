package com.hhplus.concert_ticketing.app.domain.payment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PAYMENT")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "reservation_id",nullable = false)
    private Long reservationId;

    @Column(name = "amount",nullable = false)
    private Integer amount;

    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at",nullable = false)
    private LocalDateTime updateAt;

    public Payment(Long userId, Long reservationId, Integer amount) {
        this.userId = userId;
        this.reservationId = reservationId;
        this.status = PaymentStatus.PROGRESS;
        this.amount = amount;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }
    public void finishPayment(){
        this.status = PaymentStatus.DONE;
        this.updateAt = LocalDateTime.now();
    }
}
