package com.example.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AuditConfig implements AuditorAware<String> {

    private final HttpSession session;

    @Override
    public Optional<String> getCurrentAuditor() {
        String userId = (String) session.getAttribute("id");
        return Optional.ofNullable(userId);
    }
}
