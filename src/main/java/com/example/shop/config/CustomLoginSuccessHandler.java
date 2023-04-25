package com.example.shop.config;

import com.example.shop.util.CookieUtil;
import com.example.shop.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인 성공시 jwt accesToken과 refreshToken을 생성합니다.
 */
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess
            (HttpServletRequest request,
             HttpServletResponse response,
             Authentication authentication) throws IOException, ServletException {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        // accessToken 생성
        String accessJwt = JwtUtil.createJwt(userDetails.getEmail(), JwtUtil.ACEESS_TOKEN_EXPIRATION);
        Cookie accessTokenCookie = CookieUtil.createJwtCookie(JwtUtil.ACCESS_TOKEN, accessJwt);
        // refreshToken 생성
        String refreshJwt = JwtUtil.createJwt(userDetails.getEmail(), JwtUtil.REFRESH_TOKEN_EXPIRATION);
        Cookie refreshTokenCookie = CookieUtil.createJwtCookie(JwtUtil.REFRESH_TOKEN, refreshJwt);
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        response.sendRedirect("/");
    }
}
