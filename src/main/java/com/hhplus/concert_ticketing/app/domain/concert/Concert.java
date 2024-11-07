package com.hhplus.concert_ticketing.app.domain.concert;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor // 파라미터가 없는 디폴트 생성자를 생성
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자를 생성
@Table(name = "CONCERT")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "reservation_start_at", nullable = false)
    private LocalDateTime reservationStartAt;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    public Concert(String title, LocalDateTime reservationStartAt, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.reservationStartAt = reservationStartAt;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
