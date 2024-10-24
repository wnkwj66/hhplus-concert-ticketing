package com.hhplus.concert_ticketing.domain.concert;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SEAT")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "performance_id",nullable = false)
    private Long performanceId;

    @Column(name = "seat_no",nullable = false)
    private Integer seatNo;

    @Column(name = "price",nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private SeatStatus status;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public void isReservedCheck(){
        if(this.status != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("해당 좌석은 예약 할 수 없습니다.");
        } else {
            this.status = SeatStatus.TEMPORARY;
            this.expiredAt = LocalDateTime.now().plusMinutes(5);
        }
    }

    public void isExpiredCheck() {
        if(LocalDateTime.now().isAfter(this.expiredAt)) {
            throw new IllegalStateException("좌석 예약이 만료되었습니다. 다시 시도해주세요.");
        }
    }

}
