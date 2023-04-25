package com.example.shop.controller;

import com.example.shop.dto.MemberDTO;
import com.example.shop.util.CookieUtil;
import com.example.shop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    public static final String LOGIN_FORM = "login/loginForm";

    @GetMapping
    public String loginForm(
            @ModelAttribute("memberDTO") MemberDTO memberDTO,
            HttpSession session,
            Model model) {
        String loginErorr = "loginError";
        String errorMessage = (String) session.getAttribute(loginErorr);
        if (errorMessage != null) {
            model.addAttribute(loginErorr, "아이디, 비밀번호를 확인해주세요.");
            session.removeAttribute(loginErorr);
        }
        return LOGIN_FORM;
    }

    @GetMapping("/logout")
    public String logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        CookieUtil.getCookie(request, JwtUtil.ACCESS_TOKEN)
                .ifPresent(cookie -> {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                    SecurityContextHolder.clearContext();
                });

        return LoginController.LOGIN_FORM;
    }
}
