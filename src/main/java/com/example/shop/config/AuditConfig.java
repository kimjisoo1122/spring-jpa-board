package com.example.shop.config;

import com.example.shop.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AuditConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication instanceof CustomUserDetails) {
            CustomUserDetails member = (CustomUserDetails) authentication.getPrincipal();
            return Optional.of(member.getMemberId().toString());
        } else {
            return Optional.of("test");
        }
    }
}
