package com.example.shop.config;

import com.example.shop.util.CookieUtil;
import com.example.shop.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final CustomUserDetailService userDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 쿠키에 저장된 accessToken을 가져옵니다.
        String accessToken = CookieUtil.getCookie(request, JwtUtil.ACCESS_TOKEN)
                .map(Cookie::getValue)
                .orElse("");
        if (accessToken.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // accessToken을 파싱하여 인증토큰을 생성합니다.
        try {
            Claims accessClaims = JwtUtil.parseJwt(accessToken);
            String email = accessClaims.getSubject();
            UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(email);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (ExpiredJwtException e) {
            try {
                CookieUtil.getCookie(request, JwtUtil.REFRESH_TOKEN)
                        .map(Cookie::getValue)
                        .map(JwtUtil::parseJwt)
                        .ifPresent(refreshClaim -> {
                            // 리프레쉬토큰이 유효하면 accessToken 재발급
                            String email = refreshClaim.getSubject();
                            String newAccessToken = JwtUtil.createJwt(email, JwtUtil.ACEESS_TOKEN_EXPIRATION);
                            Cookie newJwtCookie = CookieUtil.createJwtCookie(JwtUtil.ACCESS_TOKEN, newAccessToken);
                            response.addCookie(newJwtCookie);

                            // 리프레쉬토큰 재발급
                            String newRefreshToken = JwtUtil.createJwt(email, JwtUtil.REFRESH_TOKEN_EXPIRATION);
                            Cookie newRefreshCookie = CookieUtil.createJwtCookie(JwtUtil.REFRESH_TOKEN, newRefreshToken);
                            response.addCookie(newRefreshCookie);

                            UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(email);
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        });
            } catch (ExpiredJwtException ex) {
                filterChain.doFilter(request, response);
            }
        }
        filterChain.doFilter(request, response);
    }
    private UsernamePasswordAuthenticationToken getAuthenticationToken(String email) {
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}
