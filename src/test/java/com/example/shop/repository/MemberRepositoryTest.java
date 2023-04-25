package com.example.shop.repository;

import com.example.shop.entity.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 이메일로회원조회() throws Exception {
        //given
        String email = "test@test.com";
        //when
        Member member = memberRepository.findMemberByEmail(email).orElse(null);
        //then
        Assertions.assertNotNull(member);
    }

}