package com.hhplus.concert_ticketing.app.application;

import com.hhplus.concert_ticketing.app.domain.concert.*;
import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.domain.payment.PaymentRepository;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueRepository;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.infra.repository.concert.JpaSeatRepository;
import io.jsonwebtoken.Claims;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ConcertUseCase {
    private final ConcertRepository concertRepository;
    private final ConcertPerformanceRepository performanceRepository;
    private final QueueRepository queueRepository;
    private final ReservationRepository reservationRepository;
    private final PaymentRepository paymentRepository;
    private final JpaSeatRepository jpaSeatRepository;
    private final RedissonClient redissonClient;

    private static final Logger logger = LoggerFactory.getLogger(ConcertUseCase.class);

    public List<Concert> selectConcertList() {
        return concertRepository.selectConcertList(LocalDateTime.now(),LocalDate.now());
    }

    public List<ConcertPerformance> selectPerformances(Long concertId) {
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

        Seat seat = jpaSeatRepository.findById(seatId).orElseThrow();
        seat.isReservedCheck();

        ConcertPerformance performance = performanceRepository.findById(performanceId);
        performance.isSoldOutCheck();
        Concert concert = concertRepository.findById(performance.getConcertId());

        Reservation reservation = new Reservation(userId,concert,performance, seat);
        reservationRepository.save(reservation);

        return reservation;
    }

    // 예약 시스템 낙관적 락 적용
    @Transactional
    @Retryable(value = OptimisticLockException.class,
            maxAttempts = 5,
            backoff = @Backoff(delay = 100))
    public Reservation reserveConcertOptimisticLock(String token, Long performanceId, Long seatId) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

//        Queue queue = queueRepository.findByToken(token);
//        queue.isValidCheck();

        Seat seat = jpaSeatRepository.findById(seatId).orElseThrow();
        seat.isReservedCheck();

        ConcertPerformance performance = performanceRepository.findById(performanceId);
        performance.isSoldOutCheck();
        Concert concert = concertRepository.findById(performance.getConcertId());

        Reservation reservation = new Reservation(userId,concert,performance, seat);
        reservationRepository.save(reservation);

        return reservation;
    }

    // 예약 시스템 비관적 락 적용
    @Transactional
    public Reservation reserveConcertPessimisticLock(String token, Long performanceId, Long seatId) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);
//
//        Queue queue = queueRepository.findByToken(token);
//        queue.isValidCheck();

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
    public Reservation reserveConcertRedisLock(String token, Long performanceId, Long seatId) {
        Claims claims = Users.parseJwtToken(token);
        Long userId = claims.get("userId", Long.class);

        // Redis 분산 락 설정
        RLock lock = redissonClient.getLock("reservationLock:" + performanceId + ":" + seatId); // 공연 및 좌석에 대한 고유 락 생성

        try {
            // 최대 5초 대기 후 1초 동안 락 유지
            if (lock.tryLock(5, 1, TimeUnit.SECONDS)) {
                logger.info("락 획득 성공: userId={}, performanceId={}, seatId={}", userId, performanceId, seatId);

//                Queue queue = queueRepository.findByToken(token);
//                queue.isValidCheck();

                Seat seat = jpaSeatRepository.findById(seatId).orElseThrow();
                seat.isReservedCheck();

                ConcertPerformance performance = performanceRepository.findById(performanceId);
                performance.isSoldOutCheck();
                Concert concert = concertRepository.findById(performance.getConcertId());

                Reservation reservation = new Reservation(userId, concert, performance, seat);
                reservationRepository.save(reservation);

                return reservation;
            } else {
                logger.warn("락 획득 실패: userId={}, performanceId={}, seatId={}", userId, performanceId, seatId);
                throw new RuntimeException("좌석 예약 중에 다른 요청이 처리되었습니다.");
            }
        } catch (InterruptedException e) {
            logger.error("락 대기 중 인터럽트 발생: userId={}, performanceId={}, seatId={}", userId, performanceId, seatId, e);
            Thread.currentThread().interrupt();
            throw new RuntimeException("Redis 락 획득 중 오류 발생", e);
        } finally {
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
                logger.info("락 해제 완료: userId={}, performanceId={}, seatId={}", userId, performanceId, seatId);
            }
        }
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


    @Transactional
    public void setConcert() {
        Concert concert1 =  concertRepository.save(new Concert("A 콘서트", LocalDateTime.now().minusDays(2), LocalDate.now(), LocalDate.now().plusDays(3)));
        ConcertPerformance performance1 = performanceRepository.save(new ConcertPerformance(concert1.getId(),LocalDateTime.now().plusHours(5),1,50 ));
        Seat seat1 = jpaSeatRepository.save(new Seat(performance1.getId(),1,30000,SeatStatus.AVAILABLE));

        Concert concert2 =  concertRepository.save(new Concert("B 콘서트", LocalDateTime.now().minusDays(2), LocalDate.now(), LocalDate.now().plusDays(3)));
        ConcertPerformance performance2 = performanceRepository.save(new ConcertPerformance(concert2.getId(),LocalDateTime.now().plusHours(5),1,50 ));
        Seat seat2 = jpaSeatRepository.save(new Seat(performance2.getId(),1,30000,SeatStatus.AVAILABLE));

        Concert concert3 =  concertRepository.save(new Concert("C 콘서트", LocalDateTime.now().minusDays(2), LocalDate.now(), LocalDate.now().plusDays(3)));
        ConcertPerformance performance3 = performanceRepository.save(new ConcertPerformance(concert3.getId(),LocalDateTime.now().plusHours(5),1,50 ));
        Seat seat3 = jpaSeatRepository.save(new Seat(performance3.getId(),1,30000,SeatStatus.AVAILABLE));
    }
}
