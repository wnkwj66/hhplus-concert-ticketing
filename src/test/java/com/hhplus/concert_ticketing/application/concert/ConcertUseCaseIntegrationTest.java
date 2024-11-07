package com.hhplus.concert_ticketing.application.concert;

import com.hhplus.concert_ticketing.app.application.ConcertUseCase;
import com.hhplus.concert_ticketing.app.domain.concert.*;
import com.hhplus.concert_ticketing.app.domain.payment.Payment;
import com.hhplus.concert_ticketing.app.domain.queue.Queue;
import com.hhplus.concert_ticketing.app.domain.queue.QueueStatus;
import com.hhplus.concert_ticketing.app.domain.user.Point;
import com.hhplus.concert_ticketing.app.domain.user.Users;
import com.hhplus.concert_ticketing.app.infra.repository.concert.JpaConcertRepository;
import com.hhplus.concert_ticketing.app.infra.repository.concert.JpaPerformanceRepository;
import com.hhplus.concert_ticketing.app.infra.repository.concert.JpaSeatRepository;
import com.hhplus.concert_ticketing.app.infra.repository.queue.JpaQueueRepository;
import com.hhplus.concert_ticketing.app.infra.repository.user.JpaPointRepository;
import com.hhplus.concert_ticketing.app.infra.repository.user.JpaUsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ConcertUseCaseIntegrationTest {
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
    private JpaUsersRepository jpaUserRepository;

    @Autowired
    private JpaPointRepository jpaPointRepository;

    private String token;
    private Long userId;
    private Queue queue;

    @BeforeEach
    public void setUp() {
        // JWT 토큰 생성 (예: userId = 1)
        userId = 1L;

        // 필요한 테스트 데이터 생성
        Concert concert = jpaConcertRepository.save(new Concert(1L, "Concert A", LocalDateTime.of(2024, 10, 20, 12, 0, 0), LocalDate.of(2024, 10, 30), LocalDate.of(2024, 11, 22)));

        ConcertPerformance performance = jpaPerformanceRepository.save(new ConcertPerformance(1L, concert.getId(), ConcertStatus.AVAILABLE,LocalDateTime.now().plusDays(1),10,50));

        Seat seat = jpaSeatRepository.save(new Seat(performance.getId(), 15, 50000, SeatStatus.AVAILABLE));

        token = Queue.generateJwtToken(userId, concert.getId(),performance.getId());
        // 테스트용 Queue 생성
        queue = new Queue(userId,concert.getId(),performance.getId(), token ,QueueStatus.ACTIVE);
        jpaQueueRepository.save(queue);
    }
    @AfterEach
    void cleanUp() {
        jpaSeatRepository.deleteAll();
        jpaPerformanceRepository.deleteAll();
        jpaConcertRepository.deleteAll();
    }

    @Test
    void 콘서트_조회_기능() {
        List<Concert> concertList = concertUseCase.selectConcertList();

        assertEquals(1, concertList.size());
        assertEquals("Concert A", concertList.get(0).getTitle());
    }

    @Test
    void 콘서트_회차_조회_기능() {
        List<ConcertPerformance> performancesList = concertUseCase.selectPerformances(1L);

        assertEquals(1, performancesList.size());
    }

    @Test
    void 예매가능한_좌석조회_기능() {
        List<Seat> seatList = concertUseCase.selectSeats(1L);
        assertEquals(1, seatList.size());
    }

    @Test
    @DirtiesContext
    @DisplayName("예약 및 결제 진행과정 테스트")
    void 결제_진행_성공_테스트() {
        // given
        Users user = jpaUserRepository.save(new Users(1L,"테스터"));
        Point userPoint = new Point(user.getId(), 10000);
        jpaPointRepository.save(userPoint);

        // when
        Reservation reservation = concertUseCase.reserveConcert(token, 1L,1L);
        Payment payment = concertUseCase.paymentReservation(token, 1L);

        // then
        assertEquals(50000,payment.getAmount());
        assertEquals(1L, reservation.getSeatId());
        assertEquals(ReservationStatus.TEMPORARY, reservation.getStatus());
    }
}