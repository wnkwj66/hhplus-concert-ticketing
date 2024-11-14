package com.hhplus.concert_ticketing.app.domain.user;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Base64;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class Users {
    private static final String SECRET_KEY = Base64.getEncoder().encodeToString("7v3Y8xD3k6W1rTqH4z5NqD7s6Jv2F8x3".getBytes());
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "point_id" ,nullable = false)
    private Long pointId;

    @Column(name = "name" ,nullable = false)
    private String name;

    public Users(Long pointId, String name) {
        this.pointId = pointId;
        this.name = name;
    }


    public static Claims parseJwtToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody(); // 토큰의 페이로드에서 클레임(Claims) 추출

        return  claims;
    }

}
