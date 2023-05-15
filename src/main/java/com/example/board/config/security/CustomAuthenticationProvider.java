package com.example.board.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final CustomUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication.isAuthenticated()) {
            return authentication;
        } else {
            String email = authentication.getName();
            String password = authentication.getCredentials().toString();
            UserDetails userDetails = userDetailService.loadUserByUsername(email);
            String encodedPassword = userDetails.getPassword();
            if (passwordEncoder.matches(password, encodedPassword)) {
                return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
            } else {
                throw new BadCredentialsException("아이디, 비밀번호를 확인해주세요.");
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
