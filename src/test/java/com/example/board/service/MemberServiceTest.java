package com.example.board.service;

import com.example.board.TestDataUtil;
import com.example.board.dto.MemberDto;
import com.example.board.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원가입")
    void join() throws Exception {
        //given
        MemberDto memberDto = TestDataUtil.getTestMemberDto();
        //when
        Long memberId = memberService.join(memberDto);
        Member member = memberService.findById(memberId).orElse(null);
        //then
        assertThat(member).isNotNull();
        assertThat(member.getName()).isEqualTo(memberDto.getName());
        assertThat(member.getEmail()).isEqualTo(memberDto.getEmail());
    }
}