package com.example.shop.config;

import com.example.shop.exception.JwtAuthenticationException;
import com.example.shop.util.CookieUtil;
import com.example.shop.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(permit(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie cookie = CookieUtil.getCookie(request, JwtUtil.ACCESS_TOKEN);
        String accessToken = cookie.getValue();

        // 헤더에 토큰이 없거나 토큰의 이름이 JWT양식이 아닌 경우 Login
        if (accessToken.isBlank()) {
            log.info("인증된 사용자가 아닙니다.");
            // 인증이 필요하면 AuthenticationException가 발생되어 AuthenticationEntryPoint로 이동한다
            // default entryPoint는 login페이지로 이동시킨다.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader(HttpHeaders.LOCATION, "/");
        }
        Claims claims = JwtUtil.parseJwt(accessToken);
        // JWT 만료 확인
        if (JwtUtil.isExpired(claims)) {
            log.info("JWT가 만료 되었습니다.");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setHeader(HttpHeaders.LOCATION, "/");
        }

        String memberId = claims.getSubject();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private boolean permit(String uri) {
        List<String> permitUri = List.of("/", "/login", "/member/sign");
        return permitUri.contains(uri);
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public void handleJwtAuthentication(HttpServletResponse response, JwtAuthenticationException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.sendRedirect("/");
    }
}
