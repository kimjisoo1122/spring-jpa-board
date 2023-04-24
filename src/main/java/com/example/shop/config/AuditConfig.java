package com.example.shop.config;

import com.example.shop.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AuditConfig implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();

        if (principal instanceof Member) {
            Member member = (Member) principal;
            return Optional.ofNullable(member.getName());
        } else {
            return Optional.of("visitor");
        }
    }
}
