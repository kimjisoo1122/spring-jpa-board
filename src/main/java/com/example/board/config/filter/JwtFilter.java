package com.example.board.config.filter;

import com.example.board.config.security.CustomUserDetailService;
import com.example.board.util.CookieUtil;
import com.example.board.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
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

        log.info("jwt filter request = {}", request.getRequestURI());
        // 쿠키에 저장된 Token을 가져옵니다.
        String accessToken = CookieUtil.getCookieValue(request, JwtUtil.ACCESS_TOKEN);
        String refreshToken = CookieUtil.getCookieValue(request, JwtUtil.REFRESH_TOKEN);

        // 엑세스토큰 존재
        if (StringUtils.hasText(accessToken)) {
            try {
                Claims accessClaims = JwtUtil.parseJwt(accessToken);
                String userEmail = accessClaims.getSubject();
                // 엑세스토큰에서 유저이메일 추출하여 인증처리
                UsernamePasswordAuthenticationToken authenticationToken = this.getAuthenticationToken(request, response, userEmail);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (JwtException jwtException) {
                // 엑세스토큰이 유효하지 않은경우 (비정상토큰, 기간만료)
                try {
                    // 리프레쉬토큰 여부 확인 후 리프레쉬토큰으로 인증
                    // 인증 후 엑세스토큰, 리프레쉬토큰 재발급
                    if (StringUtils.hasText(refreshToken)) {
                        authenticationRefreshToken(request, response, refreshToken);
                    }
                } catch (JwtException ex) {
                    // 토큰이 유효하지 않아  인증객체가 없는 경우 로그인 페이지로 이동
                    filterChain.doFilter(request, response);
                }
            }
        } else if (StringUtils.hasText(refreshToken)) {
            // 리프레쉬 토크만 있는 경우 인증 후 쿠키 재발급
            try {
                authenticationRefreshToken(request, response, refreshToken);
            } catch (JwtException ex) {
                // 토큰이 유효하지 않아  인증객체가 없는 경우 로그인 페이지로 이동
                filterChain.doFilter(request, response);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void authenticationRefreshToken(HttpServletRequest request, HttpServletResponse response, String refreshToken) throws JwtException{
        // 리프레쉬토큰으로 인증
        Claims refreshClaim = JwtUtil.parseJwt(refreshToken);
        String email = refreshClaim.getSubject();
        UsernamePasswordAuthenticationToken authenticationToken = this.getAuthenticationToken(request, response, email);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 엑세스토큰 재발급
        String newAccessToken = JwtUtil.createJwt(email, JwtUtil.ACEESS_TOKEN_EXPIRATION);
        Cookie newJwtCookie = CookieUtil.createJwtCookie(JwtUtil.ACCESS_TOKEN, newAccessToken);
        response.addCookie(newJwtCookie);

        // 리프레쉬토큰 재발급
        String newRefreshToken = JwtUtil.createJwt(email, JwtUtil.REFRESH_TOKEN_EXPIRATION);
        Cookie newRefreshCookie = CookieUtil.createJwtCookie(JwtUtil.REFRESH_TOKEN, newRefreshToken);
        response.addCookie(newRefreshCookie);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request, HttpServletResponse response, String email) throws BadCredentialsException {
        try {
            UserDetails userDetails = userDetailService.loadUserByUsername(email);
            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        } catch (BadCredentialsException badCredentialsException) {
            CookieUtil.removeCookie(request, response, JwtUtil.ACCESS_TOKEN);
            CookieUtil.removeCookie(request, response, JwtUtil.REFRESH_TOKEN);
            throw badCredentialsException;
        }
    }
}