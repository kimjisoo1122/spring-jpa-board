package com.example.shop.config.security;

import com.example.shop.entity.Member;
import com.example.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws BadCredentialsException{

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("아이디, 비밀번호를 확인해주세요."));

        return CustomUserDetails.builder()
                .memberId(member.getId())
                .username(member.getName())
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority(member.getUserRole().name())))
                .build();
    }
}
