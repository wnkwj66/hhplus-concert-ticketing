# ERD 설계
## 1. ERD
```mermaid

---
title: 콘서트 예매 서비스
---
%% generates: 1:1 관계에서 필수 
%% reserves: 0 or 1 관계 (옵셔널)
%% places: 1:N 관계에서 1 ~ N개
%% has: 1:N 관계에서 0 ~ N개 (옵셔널)
erDiagram
    User ||--o{ Queue : has
    User ||--|| Point : generates
    Point ||--o{ PointHistory : has
    Concert ||--|{ Performance : places
    Performance ||--o{ Seat : has
    User ||--o{ Reservation : has
    %% 결제 취소를 고려하여 1:N으로 구현
    %% 예약은 반드시 하나의 결제를 가지고있어야 하나? --> 좌석예약 시 결제가 이루어지기 때문에 있어야 한다?
    %% 결제취소를 고려한다면 결제 테이블에 상태값이 있어야할까 ? --> 결제 취소구현 방법 고민 1.열삭제 , 2. 상태값 변경 
    Reservation ||--o{ Payment : "has"
    User ||--o{ Queue : "has"
    Concert ||--o{ Queue :"has"
    
    Queue {
        long id pk
        long userId fk
        long concertId fk
        string tokenId
        string status
        int position
        dateTime createAt
        dateTime updateAt
        dateTime expiredAt
    }
    User {
        long id pk
        long pointId fk
        string name
    }
    
    Point {
        long id pk
        int amount
    }
    
    PointHistory {
        long id pk
        long userId
        int amount
        string type
        datetime createAt
    }
    Concert {
        long id pk
        string name
        string concertStatus
        datetime reservationStartAt
        date startDate
        date endDate
    }

    Performance {
        long id pk
        long concertId fk
        datetime performanceAt
    }
    
    Seat {
        long id pk
        long performanceId fk
        int seatNo
        int price
        string seatStatus
    }
    
    Reservation {
        long id pk
        long userid fk
        long performanceId fk
        long seatId fk
        string status
        datetime reservationAt
        datetime createAt
        datetime upateAt
    }
    
    Payment {
        long id pk
        long userId fk
        long reservationId fk
        int amount
        datetime createAt
        datetime updateAt
    }
    
    
```
## 2. 관계 설명
 ### 큐(QUEUE)
 - 사용자가 여러 콘서트에 대해 동시에 예약을 시도할수있기때문에 userId, concertId 컬럼이 필요하다고 생각해 추가하였습니다.
 - User(회원)과 Queue(대기열)는 1:N 관계를 가집니다.
 - Concert(콘서트)와 Queue(대기열)는 1:N 관계를 가집니다.

 ### 콘서트 서비스
 - Concert(콘서트)와 Performance(공연)는 1:N 관계를 가집니다.
    - 콘서트별 회차정보, 공연시작시간 고려하여 작성하였습니다
 - Performance(공연)와 Seat(좌석)는 1:N 관계를 가집니다.

 ### 예약 서비스
 - Reservation(예약)은 Payment와 has(0~N) 관계를 가집니다.
   - 예약 후 결제를 진행하지 않은 경우, 결제 취소를 고려하여 작성하였습니다.

 