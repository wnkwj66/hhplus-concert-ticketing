package com.hhplus.concert_ticketing.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsersUnitTest {

    @Test
    @DisplayName("포인트 충전 테스트")
    void 포인트_충전_테스트(){
        // given
        Long userId = 1L; // 유저 직접 선언
        Point point = new Point(userId,5000);

        point.chargePoint(50000);

        assertEquals(55000,point.getAmount());

    }

    @Test
    @DisplayName("포인트 충전 실패 테스트")
    void 포인트_충전_테스트_실패_충전금액_0이거나_음수(){
        // given
        Long userId = 1L; // 유저 직접 선언
        Point point = new Point(userId,5000);

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> point.chargePoint(0));
        assertEquals("충전금액이 없습니다.",e.getMessage());

    }
}
