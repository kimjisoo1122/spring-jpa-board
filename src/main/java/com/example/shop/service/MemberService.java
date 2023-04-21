package com.example.shop.service;

import com.example.shop.dto.MemberDTO;
import com.example.shop.entity.Member;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(MemberDTO memberDTO) {
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        Member member = Member.createMember(memberDTO);
        memberRepository.save(member);
        return member.getId();
    }

    public Member findMember(Long memberId) {
        return memberRepository.findMember(memberId);
    }
}
