package com.hhplus.concert_ticketing.domain.concert;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long performanceId;

    @Column(nullable = false)
    private Long seatId;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime reservationAt;

    @Column(nullable = false)
    private LocalDateTime cearteAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

}
