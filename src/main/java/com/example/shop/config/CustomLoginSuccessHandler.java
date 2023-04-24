package com.example.shop.config;

import com.example.shop.util.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 로그인 성공시 jwt 발급
        String jwt = JwtUtil.createJwt(authentication.getName());
        // accessToken 생성
        Cookie jwtCookie = new Cookie("accessToken", jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(JwtUtil.EXPIRATION_MS.intValue());
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);

//        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        response.sendRedirect("/");
    }
}
