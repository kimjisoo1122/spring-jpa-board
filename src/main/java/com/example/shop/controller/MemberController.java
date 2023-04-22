package com.example.shop.controller;

import com.example.shop.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private static final String SIGN_FORM = "member/signForm";

    @GetMapping("/sign")

    public String signForm(
            @ModelAttribute("memberDTO") MemberDTO memberDTO) {
        return SIGN_FORM;
    }
}
