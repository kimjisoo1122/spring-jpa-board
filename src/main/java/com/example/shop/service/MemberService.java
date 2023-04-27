package com.example.shop.service;

import com.example.shop.dto.MemberDTO;
import com.example.shop.entity.Member;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long join(MemberDTO memberDTO) {
        // BCrypt 해쉬 암호화
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        // 멤버 생성
        Member member = Member.createMember(memberDTO);
        // 회원 가입
        memberRepository.save(member);
        // creatUser, updateUser jpa audit 수정
        member.setMemberAuditor(member.getId());
        return member.getId();
    }

    public Optional<Member> findById(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public boolean duplicateEmail(String email) {
        return this.findByEmail(email).isPresent();
    }
}