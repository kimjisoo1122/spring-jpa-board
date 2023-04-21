package com.example.shop.service;

import com.example.shop.entity.Member;
import com.example.shop.repository.MemberRepository;
import com.example.shop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    @Value("${jwt.secret}")
    private String secretKey;
    private Long expriedMs = 1000 * 60 * 60L; // 1Hour

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean loginCheck(String email, String rawPassword) {

        Optional<Member> optMember = memberRepository.findMemberByEmail(email);

        if (optMember.isPresent()) {
            Member member = optMember.get();
            String encodedPassword = member.getPassword();
            if (isMatchesPassword(rawPassword, encodedPassword)) {
                String jwt = JwtUtil.createJwt(member.getName(), secretKey, expriedMs);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isMatchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
