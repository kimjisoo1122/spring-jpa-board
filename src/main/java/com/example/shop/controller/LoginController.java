package com.example.shop.controller;

import com.example.shop.dto.MemberDTO;
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
            @ModelAttribute("memberDTO") MemberDTO memberDTO,
            HttpSession session,
            Model model) {
        String errorMessage = (String) session.getAttribute("error");
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
            session.removeAttribute("error");
        }
        return LOGIN_FORM;
    }
}
