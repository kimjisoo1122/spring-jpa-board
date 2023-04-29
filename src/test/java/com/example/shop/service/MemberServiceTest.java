package com.example.shop.service;

import com.example.shop.dto.MemberDTO;
import com.example.shop.entity.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입() throws Exception {
        //given
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("김지수");
        memberDTO.setPassword("test1234");
        memberDTO.setEmail("test@test.com");
        memberDTO.setPhone("010-4953-3653");
        //when
        Long memberId = memberService.join(memberDTO);
        Member member = memberService.findById(memberId).orElse(null);
        //then
        assertNotNull(member);
        assertEquals(member.getName(), memberDTO.getName());
    }
}