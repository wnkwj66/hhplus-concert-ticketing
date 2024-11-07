package com.hhplus.concert_ticketing.app.domain.concert;

import com.hhplus.concert_ticketing.app.interfaces.exception.ApiException;
import com.hhplus.concert_ticketing.app.interfaces.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.logging.LogLevel;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PERFORMANCE")
public class ConcertPerformance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "concert_id",nullable = false)
    private Long concertId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConcertStatus status;

    @Column(name = "performance_at", nullable = false)
    private LocalDateTime performanceAt;

    @Column(name = "available_seat",nullable = false)
    private Integer availableSeat;

    @Column(name = "total_seat",nullable = false)
    private Integer totalSeat;

    public ConcertPerformance(Long concertId, LocalDateTime performanceAt, Integer availableSeat, Integer totalSeat) {
        this.concertId = concertId;
        this.status = ConcertStatus.AVAILABLE;
        this.performanceAt = performanceAt;
        this.availableSeat = availableSeat;
        this.totalSeat = totalSeat;
    }

    public void isSoldOutCheck(){
        if(this.getStatus() == ConcertStatus.SOLD_OUT){
            throw new ApiException(ErrorCode.PERFORMANCE_SOLD_OUT_ERROR, LogLevel.INFO);
        }
    }
}
