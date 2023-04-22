package com.example.shop.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtil {

    @Value("${jwt.secret}")
    private static String secretKey;
    // 1시간
    private static final Long EXPIRATION_MS = 3600000L;

    public static String createJwt(String userName) {

//        Claims claims = Jwts.claims(); // jwt의 저장소 map
//        claims.put("username", userName);
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
//                .setClaims(claims)
                .setSubject(userName)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
