package com.hhplus.concert_ticketing.application;

import com.hhplus.concert_ticketing.domain.concert.*;
import com.hhplus.concert_ticketing.domain.queue.Queue;
import com.hhplus.concert_ticketing.infra.concert.JpaConcertRepository;
import com.hhplus.concert_ticketing.infra.concert.JpaPerformanceRepository;
import com.hhplus.concert_ticketing.infra.concert.JpaSeatRepository;
import com.hhplus.concert_ticketing.infra.queue.JpaQueueRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PaymentConcurrencyTest {
    @Autowired
    private ConcertUseCase concertUseCase;
    @Autowired
    private JpaConcertRepository jpaConcertRepository;
    @Autowired
    private JpaPerformanceRepository jpaPerformanceRepository;
    @Autowired
    private JpaSeatRepository jpaSeatRepository;
    @Autowired
    private JpaQueueRepository jpaQueueRepository;

    private String token;
    private Long userId;
    private Queue queue;

    @BeforeEach
    public void setUp() {
        // JWT 토큰 생성 (예: userId = 1)
        userId = 1L;
        token = Jwts.builder()
                .claim("userId", userId)
                .signWith(SignatureAlgorithm.HS256, "secretKey")
                .compact();


        // 필요한 테스트 데이터 생성
        Concert concert = jpaConcertRepository.save(new Concert(1L, "Concert A", LocalDateTime.of(2024, 10, 3, 12, 0, 0), LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 11)));

        ConcertPerformance performance = jpaPerformanceRepository.save(new ConcertPerformance(1L, concert.getId(), ConcertStatus.AVAILABLE,LocalDateTime.now().plusDays(1),10,50));

        Seat seat = jpaSeatRepository.save(new Seat(15L,performance.getId(), 15, 50000, SeatStatus.AVAILABLE, LocalDateTime.now().plusMinutes(5)));


        // 테스트용 Queue 생성
        queue = new Queue(userId, concert.getId(),performance.getId(),token);
        jpaQueueRepository.save(queue);
    }
    @Test
    void 동시_결제시_한번만_성공() throws InterruptedException {
        int numberOfPayments = 5; // 동시에 시도할 결제 수
        AtomicInteger successCount = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(numberOfPayments); // 모든 스레드가 완료될 때까지 대기

        // 스레드 풀 생성
        ExecutorService executor = Executors.newFixedThreadPool(numberOfPayments);

        for (int i = 0; i < numberOfPayments; i++) {
            executor.submit(() -> {
                try {
                    concertUseCase.paymentReservation(token, 1L);
                    successCount.incrementAndGet(); // 결제 성공 시 카운트 증가
                } catch (Exception e) {
                    // 예외 발생 시 로그 출력
                    System.out.println("Payment failed: " + e.getMessage());
                } finally {
                    latch.countDown(); // 스레드가 작업 완료 시 카운트 감소
                }
            });
        }

        latch.await(); // 모든 스레드가 작업 완료될 때까지 대기
        executor.shutdown(); // 스레드 풀 종료

        // 성공한 결제 수 검증 (1개만 성공해야 함)
        assertEquals(1, successCount.get());
    }
}
