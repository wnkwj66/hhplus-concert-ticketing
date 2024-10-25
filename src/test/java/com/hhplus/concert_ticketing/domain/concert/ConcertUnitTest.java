package com.hhplus.concert_ticketing.domain.concert;

import com.hhplus.concert_ticketing.application.ConcertUseCase;
import com.hhplus.concert_ticketing.domain.concert.*;
import com.hhplus.concert_ticketing.domain.payment.Payment;
import com.hhplus.concert_ticketing.domain.payment.PaymentStatus;
import com.hhplus.concert_ticketing.domain.user.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConcertUnitTest {
    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertUseCase concertUseCase;

    @BeforeEach
    public void setUp() {
        // Mokito 초기화 (@Mock과 @InjectMocks 어노테이션을 사용하여 의존성 주입을 설정하는 과정에서 필요)
        MockitoAnnotations.openMocks(this);


    }
    @Test
    @DisplayName("콘서트는 예매시작일 ~ 콘서트 종료일자까지 리스트에서 조회된다.")
    public void 콘서트_조회_테스트(){
        // 실제 DB에 저장되고 잘 조회해오는지까지 조회하려면, concertRepository.save(concert) 해주고 검증해야함
        // mockConcerts == result
        // given
        LocalDateTime fixedNow = LocalDateTime.of(2024, 10, 5, 12, 0, 0);
        LocalDate fixedDate = LocalDate.of(2024, 10, 5);
        List<Concert> mockConcerts = new ArrayList<>();

        //조회o- 예매오픈일 ~ 콘서트 종료일
        mockConcerts.add(new Concert(1L, "Concert A",
                LocalDateTime.of(2024, 10, 3, 12, 0, 0),    // openDate
                LocalDate.of(2024, 10, 10),   // startDate
                LocalDate.of(2024, 10, 11))); // endDate
        //조회x - 예매시작일이 되지않음
        mockConcerts.add(new Concert(2L, "Concert B",
                LocalDateTime.of(2024, 10, 8, 12, 0, 0),    // openDate
                LocalDate.of(2024, 10, 10),   // startDate
                LocalDate.of(2024, 10, 11)));  // endDate
        //조회x - 종료된 콘서트
        mockConcerts.add(new Concert(3L, "Concert C",
                LocalDateTime.of(2024, 10, 1, 12, 0, 0),    // openDate
                LocalDate.of(2024, 10, 2),    // startDate
                LocalDate.of(2024, 10, 4)));  // endDate

        // when
        when(concertRepository.selectConcertList(any(LocalDateTime.class))).thenAnswer(invocation -> {
            return mockConcerts.stream()
                    .filter(concert -> concert.getReservationStartAt().isBefore(fixedNow) || concert.getStartDate().isEqual(fixedDate))
                    .filter(concert -> concert.getEndDate().isAfter(fixedDate) || concert.getEndDate().isEqual(fixedDate))
                    .collect(Collectors.toList());
        });


        List<Concert> result = concertUseCase.selectConcertList();

        assertEquals(1, result.size());
        assertEquals("Concert A", result.get(0).getTitle());
    }

    @Test
    @DisplayName("예약가능한 공연을 조회한다 - 좌석 매진인 경우 조회 x ")
    void 예약가능공연_조회() {
        Long concertId = 1L;
        // given
        Concert mockConcert = new Concert(concertId, "xxx콘서트", LocalDateTime.now().minusDays(1),
                LocalDate.now().plusDays(1),LocalDate.now().plusDays(3));

        List<ConcertPerformance> mockPerformances = Arrays.asList(
                new ConcertPerformance(1L,concertId, ConcertStatus.AVAILABLE,LocalDateTime.now().plusDays(1),10,50),
                new ConcertPerformance(2L,concertId, ConcertStatus.SOLD_OUT,LocalDateTime.now().plusDays(1),0,50)
        );
        // when
        when(concertRepository.selectAvailablePerformance(concertId,0, ConcertStatus.AVAILABLE))
                .thenReturn(mockPerformances.stream()
                        .filter(concertPerformance -> concertPerformance.getAvailableSeat() > 0 && concertPerformance.getStatus() == ConcertStatus.AVAILABLE)
                        .collect(Collectors.toList()));
        List<ConcertPerformance> result = concertUseCase.selectPerformances(concertId);

        // then
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("공연의 예약가능 좌석을 조회한다.")
    void 예약가능좌석_조회(){
        // given
        Long concertId = 1L;
        ConcertPerformance concertPerformance = new ConcertPerformance(1L,concertId, ConcertStatus.AVAILABLE,LocalDateTime.now().plusDays(1),10,50);

        List<Seat> mockSeats = Arrays.asList(
                new Seat(1L,concertPerformance.getId(), 1, 50000, SeatStatus.AVAILABLE, LocalDateTime.now().plusMinutes(5)),
                new Seat(2L,concertPerformance.getId(), 2, 30000, SeatStatus.TEMPORARY, LocalDateTime.now().plusMinutes(5)),
                new Seat(3L,concertPerformance.getId(), 3, 40000, SeatStatus.RESERVED, LocalDateTime.now().plusMinutes(5)),
                new Seat(4L,concertPerformance.getId(), 4, 50000, SeatStatus.AVAILABLE, LocalDateTime.now().plusMinutes(5))
        );

        // when
        when(concertRepository.selectAvailableSeats(concertPerformance.getId(),SeatStatus.AVAILABLE))
                .thenReturn(mockSeats.stream()
                        .filter(seat -> seat.getStatus() == SeatStatus.AVAILABLE)
                        .collect(Collectors.toList()));

        List<Seat> result = concertUseCase.selectSeats(concertPerformance.getId());

        // then
        assertEquals(2,result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(4L, result.get(1).getId());
    }

    @Test
    @DisplayName("예약신청 시 예약상태 TEMPORARY로 설정한다")
    void 예약_테스트(){
        Long userId = 1L;
        // given
        Concert mockConcert = new Concert(1L, "Concert A", LocalDateTime.of(2024, 10, 3, 12, 0, 0), LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 11));
        ConcertPerformance mockConcertPerformance = new ConcertPerformance(1L,mockConcert.getId(), ConcertStatus.AVAILABLE,LocalDateTime.now().plusDays(1),10,50);
        Seat mockSeat = new Seat(1L,mockConcertPerformance.getId(), 1, 50000, SeatStatus.AVAILABLE, LocalDateTime.now().plusMinutes(5));

        // when
        Reservation reservation = new Reservation(userId,mockConcert,mockConcertPerformance,mockSeat);

        // then
        assertEquals(ReservationStatus.TEMPORARY, reservation.getStatus());

    }

    @Test
    @DisplayName("예약신청 시 좌석상태를 TEMPORARY로 설정한다.-좌석이 AVAILABLE일때만 예약가능하다.")
    void 좌석_상태_변경(){
        // given
        Concert mockConcert = new Concert(1L, "Concert A", LocalDateTime.of(2024, 10, 3, 12, 0, 0), LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 11));
        ConcertPerformance mockConcertPerformance = new ConcertPerformance(1L,mockConcert.getId(), ConcertStatus.AVAILABLE,LocalDateTime.now().plusDays(1),10,50);
        Seat mockSeat = new Seat(1L,mockConcertPerformance.getId(), 1, 50000, SeatStatus.AVAILABLE, LocalDateTime.now().plusMinutes(5));

        // when & then
        assertDoesNotThrow(mockSeat::isReservedCheck);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> mockSeat.isReservedCheck());
        assertEquals("해당 좌석은 예약 할 수 없습니다.",e.getMessage());
    }

    @Test
    @DisplayName("결제 테스트")
    void 결제_테스트() {
        // given
        Long userId = 1L;

        Concert mockConcert = new Concert(1L, "Concert A", LocalDateTime.of(2024, 10, 3, 12, 0, 0), LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 11));
        ConcertPerformance mockConcertPerformance = new ConcertPerformance(1L,mockConcert.getId(), ConcertStatus.AVAILABLE,LocalDateTime.now().plusDays(1),10,50);
        Seat mockSeat = new Seat(1L,mockConcertPerformance.getId(), 1, 50000, SeatStatus.AVAILABLE, LocalDateTime.now().plusMinutes(5));
        Reservation reservation = new Reservation(userId, mockConcert, mockConcertPerformance, mockSeat);

        // when
        Payment payment = new Payment(userId, reservation.getId(), mockSeat.getPrice());

        payment.finishPayment();

        // then
        assertEquals(PaymentStatus.DONE, payment.getStatus());
        assertEquals(50000, payment.getAmount());
    }

}