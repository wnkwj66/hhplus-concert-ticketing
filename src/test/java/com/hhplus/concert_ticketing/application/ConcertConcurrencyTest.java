package com.hhplus.concert_ticketing.application;

import com.hhplus.concert_ticketing.domain.concert.*;
import com.hhplus.concert_ticketing.domain.queue.Queue;
import com.hhplus.concert_ticketing.infra.concert.JpaConcertRepository;
import com.hhplus.concert_ticketing.infra.concert.JpaPerformanceRepository;
import com.hhplus.concert_ticketing.infra.concert.JpaSeatRepository;
import com.hhplus.concert_ticketing.infra.queue.JpaQueueRepository;
import com.hhplus.concert_ticketing.infra.user.JpaPointRepository;
import com.hhplus.concert_ticketing.infra.user.JpaUsersRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.AfterEach;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConcertConcurrencyTest {
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
    void 좌석_임시예약_동시성_테스트() throws InterruptedException {
        // given
        Queue queue = Queue.enterQueue(null, 1L, 1L,1L);
        int numberOfThreads = 10; // 동시 시도 횟수
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        AtomicInteger successfulReservations = new AtomicInteger();

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    concertUseCase.reserveConcert(queue.getTokenId(), 1L, 1L);
                    successfulReservations.getAndIncrement();
                } catch (Exception e) {
                    throw new IllegalStateException("에러발생");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 완료될 때까지 기다림
        executorService.shutdown();

        // 예약이 하나만 성공
        assertEquals(1, successfulReservations.get());
    }

}
