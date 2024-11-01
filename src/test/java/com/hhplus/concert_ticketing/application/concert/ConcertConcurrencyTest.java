package com.hhplus.concert_ticketing.application.concert;

import com.hhplus.concert_ticketing.application.ConcertUseCase;
import com.hhplus.concert_ticketing.domain.concert.*;
import com.hhplus.concert_ticketing.domain.queue.Queue;
import com.hhplus.concert_ticketing.domain.queue.QueueStatus;
import com.hhplus.concert_ticketing.infra.repository.concert.JpaConcertRepository;
import com.hhplus.concert_ticketing.infra.repository.concert.JpaPerformanceRepository;
import com.hhplus.concert_ticketing.infra.repository.concert.JpaReservationRepository;
import com.hhplus.concert_ticketing.infra.repository.concert.JpaSeatRepository;
import com.hhplus.concert_ticketing.infra.repository.queue.JpaQueueRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    @Autowired
    private JpaReservationRepository jpaReservationRepository;

    private String token;
    private Long userId;
    private Queue queue;
    private Seat seat;

    @BeforeEach
    public void setUp() {
        // JWT 토큰 생성 (예: userId = 1)
        userId = 1L;

        // 필요한 테스트 데이터 생성
        Concert concert = jpaConcertRepository.save(new Concert(1L, "Concert A", LocalDateTime.of(2024, 10, 20, 12, 0, 0), LocalDate.of(2024, 10, 30), LocalDate.of(2024, 11, 22)));

        ConcertPerformance performance = jpaPerformanceRepository.save(new ConcertPerformance(1L, concert.getId(), ConcertStatus.AVAILABLE,LocalDateTime.now().plusDays(1),10,50));

        seat = jpaSeatRepository.save(new Seat(performance.getId(), 15, 50000, SeatStatus.AVAILABLE));
        token = Queue.generateJwtToken(userId, concert.getId(),performance.getId());
        // 테스트용 Queue 생성
        queue = new Queue(userId,concert.getId(),performance.getId(), token , QueueStatus.ACTIVE);
        jpaQueueRepository.save(queue);
    }
    @AfterEach
    void cleanUp() {
        jpaSeatRepository.deleteAll();
        jpaPerformanceRepository.deleteAll();
        jpaConcertRepository.deleteAll();
    }
    @Test
    void 좌석_임시예약_테스트() {
        concertUseCase.reserveConcertPessimisticLock(token, 1L, 1L);
        Seat result = jpaSeatRepository.findById(seat.getId()).get();

        // then
        assertEquals(15,result.getSeatNo());
        assertEquals(SeatStatus.TEMPORARY,result.getStatus());
    }
    @Test
    void 좌석_임시예약_동시성_비관적락_테스트() throws InterruptedException {
        // given
        int numberOfThreads = 10; // 동시 시도 횟수
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        AtomicInteger successfulReservations = new AtomicInteger();

        for (int i = 0; i <= numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    concertUseCase.reserveConcertPessimisticLock(token, 1L, 1L);
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
    @Test
    void 좌석_임시예약_동시성_낙관적락_테스트() throws InterruptedException {
        // given
        int numberOfThreads = 10; // 동시 시도 횟수
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        AtomicInteger successfulReservations = new AtomicInteger();
        AtomicInteger errorReservations = new AtomicInteger();

        // when
        for (int i=0; i < numberOfThreads; i++) {
            // excute() -> 리턴 타입이 void로 Task의 실행 결과나 Task의 상태를 알 수 없다.
            executorService.execute(()->{
                try{
                    concertUseCase.reserveConcertPessimisticLock(token, 1L, 1L);
                    successfulReservations.getAndIncrement();
                } catch (Exception e) {
                    errorReservations.getAndIncrement();
                    throw new ObjectOptimisticLockingFailureException("충돌 발생 : "+ e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        // ExecutorService 종료
        executorService.shutdown();
        System.out.println("성공 횟수 : " + successfulReservations.get());
        System.out.println("실패 횟수 : " + errorReservations.get());
        // then
        assertEquals(1, successfulReservations.get());
        assertEquals(9, errorReservations.get());
    }

    @Test
    void redisLock_좌석예약_동시성테스트() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    concertUseCase.reserveConcertRedisLock(token, 1L, 1L);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await(10, TimeUnit.SECONDS);
        executorService.shutdown();
        long reservationCount = jpaReservationRepository.countByPerformanceIdAndSeatId(1L, 1L);
        assertThat(reservationCount).isEqualTo(1);
    }
}
