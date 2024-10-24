package com.hhplus.concert_ticketing.domain.concert;


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

    @Column(name = "reservationStartAt", nullable = false)
    private LocalDateTime reservationStartAt;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;
}
