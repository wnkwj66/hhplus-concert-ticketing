package com.hhplus.concert_ticketing.application;

import com.hhplus.concert_ticketing.domain.concert.*;
import com.hhplus.concert_ticketing.domain.payment.Payment;
import com.hhplus.concert_ticketing.domain.payment.PaymentRepository;
import com.hhplus.concert_ticketing.domain.queue.Queue;
import com.hhplus.concert_ticketing.domain.queue.QueueRepository;
import com.hhplus.concert_ticketing.domain.user.Users;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertUseCase {
    private final ConcertRepository concertRepository;
    private final ConcertPerformanceRepository performanceRepository;
    private final QueueRepository queueRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;

    public List<Concert> selectConcertList() {
        return concertRepository.selectConcertList(LocalDateTime.now());
    }

    public List<ConcertPerformance> selectPerformances(Long concertId) {
//        return concertRepository.selectAvailablePerformance(concertId, 0, ConcertStatus.AVAILABLE);
        return concertRepository.selectAvailablePerformance(concertId, 0);
    }

    public List<Seat> selectSeats(Long performanceId) {
        return concertRepository.selectAvailableSeats(performanceId);
    }

    @Transactional
    public Reservation reserveConcert(String token, Long performanceId, Long seatId) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Queue queue = queueRepository.findByToken(token);
        queue.isValidCheck();

        // 비관적 락을 사용하여 좌석 조회 및 예약 처리
        Seat seat = concertRepository.findByIdWithLock(seatId);
        seat.isReservedCheck();

        ConcertPerformance performance = performanceRepository.findById(performanceId);
        performance.isSoldOutCheck();
        Concert concert = concertRepository.findById(performance.getConcertId());

        Reservation reservation = new Reservation(userId,concert,performance, seat);
        reservationRepository.save(reservation);

        return reservation;
    }

    @Transactional
    public Payment paymentReservation(String token, Long reservationId) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        Queue queue = queueRepository.findByToken(token);
        queue.isValidCheck();

        Reservation reservation = reservationRepository.findById(reservationId);

        Payment payment = new Payment(userId, reservationId, reservation.getTotalAmount());
        paymentRepository.save(payment);

        queue.finishQueue();
        payment.finishPayment();

        return payment;
    }
}
