package com.example.board.service;

import com.example.board.dto.MemberDTO;
import com.example.board.entity.Member;
import com.example.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpSession session;

    public Long join(MemberDTO memberDTO) {
        // BCrypt 해쉬 암호화
        memberDTO.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        // 멤버 생성
        Member member = Member.createMember(memberDTO);
        // 회원 가입
        memberRepository.save(member);
        // Auditor 설정 (회원가입시에는 인증객체가 없어서 세션에 임시저장)
        String memberId = member.getId().toString();
        session.setAttribute("joinId", memberId);
        member.setMemberAuditor(member);
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
