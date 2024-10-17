package com.hhplus.concert_ticketing.domain.concert;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    LocalDateTime reservationStartAt;

    @Column(nullable = false)
    LocalDate startDate;

    @Column(nullable = false)
    LocalDate endDate;

    public Concert(String title, String status, LocalDateTime reservationStartAt, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.status = status;
        this.reservationStartAt = reservationStartAt;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
