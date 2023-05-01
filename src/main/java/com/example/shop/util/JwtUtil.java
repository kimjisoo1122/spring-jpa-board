package com.example.shop.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtil {

    private static String secretKey;
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final Long ACEESS_TOKEN_EXPIRATION = 1800000L; // 30분
    public static final Long REFRESH_TOKEN_EXPIRATION = 604800000L; // 7일

    @Value("${jwt.secret}")
    private void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public static String createJwt(String email, Long tokenExpiration) {
        return Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public static Claims parseJwt(String token) throws JwtException {
        String parseToken = token.replace("Bearer ", "");
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(parseToken).getBody();
    }
}
