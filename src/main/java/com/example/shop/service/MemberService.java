package com.example.shop.service;

import com.example.shop.dto.MemberDTO;
import com.example.shop.entity.Member;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(MemberDTO memberDTO) {

        // BCrypt 해쉬 암호화
        memberDTO.setPassword(BCrypt.hashpw(memberDTO.getPassword(), BCrypt.gensalt()));
        // 멤버 생성
        Member member = Member.createMember(memberDTO);
        // 회원 가입
        memberRepository.save(member);

        return member.getId();
    }

    public Member findMember(Long memberId) {
        return memberRepository.findMember(memberId);
    }
}
