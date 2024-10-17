package com.hhplus.concert_ticketing.domain.concert;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long performanceId;

    @Column(nullable = false)
    private Integer seatNo;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String status;

    public Seat(Long seatId, String status) {
        this.id = seatId;
        this.status = status;
    }
}
