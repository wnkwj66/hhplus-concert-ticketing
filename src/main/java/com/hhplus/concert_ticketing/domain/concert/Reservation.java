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
@Table(name = "RESERVATION")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private Long userId;

    @Column(name = "performance_id",nullable = false)
    private Long performanceId;

    @Column(name = "seat_id",nullable = false)
    private Long seatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private ReservationStatus status;

    @Column(name = "concert_title", nullable = false)
    private String concertTitle;

    @Column(name = "concert_start_at", nullable = false)
    private LocalDateTime concertStartAt;

    @Column(name = "seat_no", nullable = false)
    private Integer seatNo;

    @Column(name = "reservation_at",nullable = false)
    private LocalDateTime reservationAt;

    @Column(name = "create_at",nullable = false)
    private LocalDateTime createAt;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    public Reservation(Long userId, Concert concert, ConcertPerformance concertPerformance, Seat seat) {
        this.userId = userId;
        this.performanceId = concertPerformance.getId();
        this.seatId = seat.getId();
        this.status = ReservationStatus.TEMPORARY;
        this.concertTitle = concert.getTitle();
        this.concertStartAt = concertPerformance.getPerformanceAt();
        this.seatNo = seat.getSeatNo();
        this.reservationAt = LocalDateTime.now();
        this.createAt = LocalDateTime.now();
        this.totalAmount = seat.getPrice();
    }

}
