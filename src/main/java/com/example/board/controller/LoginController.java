package com.example.board.controller;

import com.example.board.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    public static final String LOGIN_FORM = "login/loginForm";

    @GetMapping
    public String loginForm(
            @ModelAttribute("memberDTO") MemberDto memberDTO,
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
}
