package com.hhplus.concert_ticketing.application.user;

import com.hhplus.concert_ticketing.application.UserUseCase;
import com.hhplus.concert_ticketing.domain.queue.Queue;
import com.hhplus.concert_ticketing.domain.user.Point;
import com.hhplus.concert_ticketing.domain.user.Users;
import com.hhplus.concert_ticketing.infra.user.JpaPointHistoryRepository;
import com.hhplus.concert_ticketing.infra.user.JpaPointRepository;
import com.hhplus.concert_ticketing.infra.user.JpaUsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserUseCaseTest {
    @Autowired
    private JpaUsersRepository jpaUsersRepository;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    @Autowired
    private JpaPointHistoryRepository jpaPointHistoryRepository;

    @Autowired
    private UserUseCase userUseCase;

    @AfterEach
    void cleanUp() {
        jpaUsersRepository.deleteAll();
        jpaPointRepository.deleteAll();
        jpaPointHistoryRepository.deleteAll();
    }
    @Test
    void 유저_잔액_충전_테스트() {
        // given
        Users users = new Users("테스트");
        jpaUsersRepository.save(users);

        Point userPoint = new Point(users.getId(), 10000);
        jpaPointRepository.save(userPoint);
        // when
        String tokenId = Queue.generateJwtToken(users.getId());
        int amount = userUseCase.chargeUserAmount(tokenId,20000);

        // then
        Point result = jpaPointRepository.findById(users.getId()).get();

        assertEquals(30000,result.getAmount());
    }

    @Test
    @DisplayName("한명의 유저가 동시에 3번 잔액 충전 시 1번만 잔액충전됨")
    public void 낙관적락_충돌_테스트() throws InterruptedException {
        // given
        Users users = new Users("테스트");
        jpaUsersRepository.save(users);

        Point userPoint = new Point(users.getId(), 10000);
        jpaPointRepository.save(userPoint);

        // when
        int threadCount =3;
        // ExecutorService -> 병렬 작업 시 여러 개의 작업을 효율적으로 처리하기 위해 제공되는 JAVA 라이브러리
        // FixedThreadPool -> 고정된 개수를 가진 쓰레드풀
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successfulReservations = new AtomicInteger();
        // when & then
        for (int i=0; i < threadCount; i++) {
            // 각 스레드에서 토큰을 생성
            String tokenId = Queue.generateJwtToken(users.getId());
            // excute() -> 리턴 타입이 void로 Task의 실행 결과나 Task의 상태를 알 수 없다.
            executorService.execute(()->{
                try{
                    userUseCase.chargeUserAmountOptimisticLock(tokenId, 1000);
                    successfulReservations.getAndIncrement();
                } catch (ObjectOptimisticLockingFailureException e) {
                    throw new ObjectOptimisticLockingFailureException("충돌 발생 : "+ e.getMessage(), e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();


        // 포인트 최종 잔액 확인
        Point finalPoint = jpaPointRepository.findById(users.getId()).orElse(null);
        assertEquals(10000 + 1000, finalPoint.getAmount());

        // ExecutorService 종료
        executorService.shutdown();
        assertEquals(1, successfulReservations.get());
    }
}