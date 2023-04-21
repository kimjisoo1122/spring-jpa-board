package com.example.shop.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtil {

    public static String createJwt(String userName, String securityKey, Long expiredMs) {
        Claims claims = Jwts.claims(); // jwt의 저장소 map
        claims.put("username", userName);
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS256, securityKey)
                .compact();
    }
}
