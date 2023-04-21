package com.example.shop.controller;

import com.example.shop.dto.MemberDTO;
import com.example.shop.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    private static final String LOGIN_FORM = "login/loginForm";

    @GetMapping
    public String loginForm(
            @ModelAttribute("memberDTO") MemberDTO memberDTO) {
        return LOGIN_FORM;
    }

    @PostMapping
    public String login(
            @Valid MemberDTO memberDTO,
            BindingResult bindingResult) {

        // 이메일, 패스워드 NotBlank 이메일 유효성검증
        if (bindingResult.hasErrors()) {
            return LOGIN_FORM;
        }

        // 이메일, 패스워드 확인
        if (!loginService.loginCheck(memberDTO.getEmail(), memberDTO.getPassword())) {
            bindingResult.rejectValue("email", "", "아이디, 비밀번호를 확인해주세요");
            return LOGIN_FORM;
        }
        return "redirect:/";
    }
}
