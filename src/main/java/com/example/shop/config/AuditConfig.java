package com.example.shop.config;

import com.example.shop.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class AuditConfig implements AuditorAware<String> {

    private final HttpSession session;

    @Override
    public Optional<String> getCurrentAuditor() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Object principal = Optional.ofNullable(context.getAuthentication())
                .map(Authentication::getPrincipal)
                .orElse(null);

        // 회원가입의 경우 유일하게 memberId를 저장하기전에 값을 조회하지 못하여
        // 저장 후 세션에서 id를 가져와서 업데이트 해줍니다.
        Object joinId = session.getAttribute("joinId");

        if (principal instanceof CustomUserDetails) {
            CustomUserDetails member = (CustomUserDetails) authentication.getPrincipal();
            return Optional.of(member.getMemberId().toString());
        } else if (joinId != null) {
            session.removeAttribute("joinId");
            return Optional.of((String) joinId);
        } else {
            return Optional.of("test");
        }
    }
}
