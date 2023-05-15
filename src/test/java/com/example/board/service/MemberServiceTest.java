package com.example.board.service;

import com.example.board.dto.MemberDto;
import com.example.board.entity.Member;
import com.example.board.TestDataUtil;
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
        MemberDto testMemberDto = TestDataUtil.getTestMemberDTO();
        //when
        Long memberId = memberService.join(testMemberDto);
        Member member = memberService.findById(memberId).orElse(null);
        //then
        assertNotNull(member);
        assertEquals(member.getName(), testMemberDto.getName());
    }
}