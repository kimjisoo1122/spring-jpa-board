package com.example.shop.controller;

import com.example.shop.dto.MemberDTO;
import com.example.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private static final String SIGN_FORM = "member/signForm";

    @GetMapping("/sign")
    public String signForm(
            @ModelAttribute("memberDTO") MemberDTO memberDTO) {
        return SIGN_FORM;
    }
    @PostMapping("/sign")
    public String sign(
            @ModelAttribute("memberDTO")
            @Valid MemberDTO memberDTO,
            BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return SIGN_FORM;
        }
        if (memberService.duplicateEmail(memberDTO.getEmail())) {
            bindingResult.rejectValue("email", "", "중복된 이메일 입니다.");
            return SIGN_FORM;
        }
        memberService.join(memberDTO);
        return LoginController.LOGIN_FORM;
    }
}
