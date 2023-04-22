package com.example.shop.service;

import com.example.shop.entity.Member;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {


    private final MemberRepository memberRepository;

    public boolean loginCheck(String email, String rawPassword) {

        Optional<Member> optMember = memberRepository.findMemberByEmail(email);

        if (optMember.isPresent()) {
            Member member = optMember.get();
            String encodedPassword = member.getPassword();
            if (isMatchesPassword(rawPassword, encodedPassword)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isMatchesPassword(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }
}
