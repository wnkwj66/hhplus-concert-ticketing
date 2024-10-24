package com.hhplus.concert_ticketing.domain.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "point_id" ,nullable = false)
    private Long pointId;

    @Column(name = "name" ,nullable = false)
    private String name;


    public static Claims parseJwtToken(String token) {
        Claims claims = Jwts.parser()
                .parseClaimsJws(token)
                .getBody(); // 토큰의 페이로드에서 클레임(Claims) 추출

        return  claims;
    }

}
