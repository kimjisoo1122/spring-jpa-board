package com.example.shop.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        // 헤더에 토큰이 없거나 토큰의 이름이 jwt양식이 아닌 경우 필터
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("jwt가 유효하지 않습니다");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.replace("Bearer ", "");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();

            // jwt 만료 확인
            if (claims.getExpiration().before(new Date())) {
                log.error("jwt의 유효기간이 만료 되었습니다.");
                filterChain.doFilter(request, response);
            }

            String username = claims.getSubject();

//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    username,
//                    null,
//                    List.of(new SimpleGrantedAuthority("USER")));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
//            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
