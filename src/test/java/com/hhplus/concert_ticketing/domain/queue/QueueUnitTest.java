package com.hhplus.concert_ticketing.domain.queue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class QueueUnitTest {
    /** 유저 대기열 토큰 발급 API 유닛 테스트 코드 */
    @Test
    @DisplayName("대기열 토큰 생성한다.")
    void 토큰_생성_테스트(){
        // given
        Long userId = 1L;
        Long concertId = 1L;
        Long performanceId = 1L;

        // when
        Queue queue = Queue.enterQueue(null,userId,concertId,performanceId);

        // then
        assertEquals(QueueStatus.WAITING, queue.getStatus());
        assertEquals(userId, queue.getUserId());
        assertEquals(concertId, queue.getConcertId());
        assertEquals(performanceId, queue.getPerformanceId());
    }


    @Test
    @DisplayName("대기열에 토큰이 있는경우 기존 토큰을 조회한다.")
    void 기존_토큰_조회(){
        // given
        Long concertId1 = 1L;
        Long performanceId1 = 1L;
        Queue queue1 = Queue.enterQueue(null,1L,concertId1,performanceId1);
        String tokenId = queue1.getTokenId();

        // 다른 콘서트
        Long concertId2 = 2L;
        Queue queue2 = Queue.enterQueue(null,1L,concertId1,performanceId1);

        // 같은 콘서트 다른 회차
        Long performanceId2 = 2L;
        Queue queue3 = Queue.enterQueue(null,1L,concertId1,performanceId2);


        // when
        List<Queue> queues1 = new ArrayList<>(Arrays.asList(
                Queue.enterQueue(null, 2L, concertId1, performanceId1),
                Queue.enterQueue(null, 3L, concertId1, performanceId1),
                Queue.enterQueue(null, 4L, concertId1, performanceId1)
        ));
        queues1.add(queue1);

        // 다른 콘서트
        List<Queue> queues2 = new ArrayList<>(Arrays.asList(
                Queue.enterQueue(null, 2L, concertId2, performanceId1)
        ));
        queues2.add(queue2);

        // 같은 콘서트 다른 회차
        List<Queue> queues3 = new ArrayList<>(Arrays.asList(
                Queue.enterQueue(null, 2L, concertId1, performanceId1),
                Queue.enterQueue(null, 4L, concertId1, performanceId1)
        ));
        queues3.add(queue3);

        // then
        assertEquals(4,queues1.size()); // 콘서트1 대기번호
        assertEquals(tokenId,queues1.get(3).getTokenId());
        assertEquals(2,queues2.size()); // 콘서트2 대기번호
        assertEquals(3,queues3.size()); // 콘서트3 대기번호
    }
    @Test
    @DisplayName("토큰 유효성 검증 - 성공")
    void 토큰_유효성_검증_성공_테스트(){
        // given
        Queue queue = new Queue(1L, 1L, 1L, 1L, "test-token",QueueStatus.WAITING , LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now().plusMinutes(5)); // 토큰 생성

        // when & then
        assertTrue(queue.isValidateToken());
    }

    @Test
    @DisplayName("토큰 유효성 검증 - 토큰이 상태가 EXPIRED인 경우 실패")
    void 토큰상태_EXPIRED_실패_테스트(){
        // given
        Queue queue = new Queue(1L, 1L, 1L, 1L, "test-token",QueueStatus.EXPIRED , LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now()); // 토큰 생성

        // when & then
        assertFalse(queue.isValidateToken());
    }

    @Test
    @DisplayName("토큰 유효성 검증 - 만료시간이 5분 초과한 경우 실패")
    void 토큰_만료시간_초과_실패_테스트(){
        // given
        Queue queue = new Queue(1L, 1L, 1L, 1L, "test-token",QueueStatus.WAITING , LocalDateTime.now(),LocalDateTime.now(),LocalDateTime.now().minusMinutes(10)); // 토큰 생성

        // when & then
        assertFalse(queue.isValidateToken());
    }
}
