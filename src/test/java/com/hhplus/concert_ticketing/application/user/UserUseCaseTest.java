package com.hhplus.concert_ticketing.application.user;

import com.hhplus.concert_ticketing.app.application.UserUseCase;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.infra.repository.user.JpaPointHistoryRepository;
import com.hhplus.concert_ticketing.app.infra.repository.user.JpaPointRepository;
import com.hhplus.concert_ticketing.app.infra.repository.user.JpaUsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserUseCaseTest {
    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    @Autowired
    private JpaPointHistoryRepository jpaPointHistoryRepository;

    @Autowired
    private UserUseCase userUseCase;

    private Users user;
    private String tokenId;


    @BeforeEach
    void setUp() {
        user = new Users("테스트");
        jpaUsersRepository.save(user);
        tokenId = Queue.generateJwtToken(user.getId());

        Point userPoint = new Point(user.getId(), 10000);
        jpaPointRepository.save(userPoint);
    }

    @AfterEach
    void cleanUp() {
        jpaUsersRepository.deleteAll();
        jpaPointRepository.deleteAll();
        jpaPointHistoryRepository.deleteAll();
    }
    @Test
    void 유저_잔액_충전_테스트() {
        // when
        int amount = userUseCase.chargeUserAmount(tokenId,20000);
        // then
        Point result = jpaPointRepository.findById(user.getId()).get();

        assertEquals(30000,result.getAmount());
    }

    @Test
    @DisplayName("한명의 유저가 동시에 3번 잔액 충전 시 1번만 잔액충전됨")
    public void 낙관적락_충돌_테스트() throws InterruptedException {
        // given
        int threadCount =10;
        // ExecutorService -> 병렬 작업 시 여러 개의 작업을 효율적으로 처리하기 위해 제공되는 JAVA 라이브러리
        // FixedThreadPool -> 고정된 개수를 가진 쓰레드풀
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successfulReservations = new AtomicInteger();
        AtomicInteger errorReservations = new AtomicInteger();

        // when
        for (int i=0; i < threadCount; i++) {
            // 각 스레드에서 토큰을 생성
            // String tokenId = Queue.generateJwtToken(users.getId());
            // excute() -> 리턴 타입이 void로 Task의 실행 결과나 Task의 상태를 알 수 없다.
            executorService.execute(()->{
                try{
                    userUseCase.chargeUserAmountOptimisticLock(tokenId, 1000);
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

        // 포인트 최종 잔액 확인
        Point finalPoint = jpaPointRepository.findById(user.getId()).orElseThrow();
        // ExecutorService 종료
        executorService.shutdown();

        // then
        System.out.println("성공 횟수 : " + successfulReservations.get());
        System.out.println("실패 횟수 : " + errorReservations.get());

        assertEquals(10000 + 1000, finalPoint.getAmount());
        assertEquals(1, successfulReservations.get());
        assertEquals(2, errorReservations.get());
    }
    @Test
    void 비관적락_테스트() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successfulReservations = new AtomicInteger();
        AtomicInteger errorReservations = new AtomicInteger();

        // when
        for(int i=0; i < threadCount; i++){
            executorService.execute(()->{
                try{
                    userUseCase.chargeUserAmountPessimisticLock(tokenId, 1000);
                    successfulReservations.getAndIncrement();
                } catch (ObjectOptimisticLockingFailureException e) {
                    errorReservations.getAndIncrement();
                    throw new ObjectOptimisticLockingFailureException("충돌 발생 : "+ e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        // 포인트 최종 잔액 확인
        Point finalPoint = jpaPointRepository.findById(user.getId()).orElseThrow();
        // ExecutorService 종료
        executorService.shutdown();

        System.out.println("성공 횟수 : " + successfulReservations.get());
        System.out.println("실패 횟수 : " + errorReservations.get());
        // then
        assertEquals(10000 + 1000 * threadCount, finalPoint.getAmount());
        assertEquals(10, successfulReservations.get());
        assertEquals(0, errorReservations.get());

    }

    @Test
    void redisLock_포인트충전_동시성테스트() throws InterruptedException {
        // given
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userUseCase.chargeUserAmountRedisLock(tokenId, 1000);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        Point updatedPoint = jpaPointRepository.findById(user.getId()).orElseThrow();

        // then: 동시 충전에도 락으로 인해 1000만 증가하는지 확인
        assertThat(updatedPoint.getAmount()).isEqualTo(1000);
    }
}