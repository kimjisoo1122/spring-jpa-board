package com.example.shop.config;

import com.example.shop.config.filter.JwtFilter;
import com.example.shop.config.security.*;
import com.example.shop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/member/sign", "/").permitAll()
//                .antMatchers(HttpMethod.POST, "/login", "member/sign").authenticated()
                .anyRequest().authenticated()
//                .anyRequest().hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .usernameParameter("email")
                .successHandler(new CustomLoginSuccessHandler())
                .failureHandler(new CustomLoginFailureHandler())
                .and()
                .logout()
                .logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies(JwtUtil.ACCESS_TOKEN)
                .deleteCookies(JwtUtil.REFRESH_TOKEN)
                .and()
                .authenticationProvider(new CustomAuthenticationProvider(customUserDetailService, passwordEncoder()))
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()

                .addFilterBefore(new JwtFilter(customUserDetailService), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 스프링시큐리티는 기본적으로 get을 제외한 http요청에 csrf토큰을 주고 받아 보안함
        // csrf공격이란 ? 해당사이트의 인증된 유저를 대상으로 타 사이트의 스크립트코드를 클릭유도하여
        // 권한을 가진 대상으로 하여금 공격대상사이트에 의도치않은 스크립트 요청
        // xss공격이란 ? 스크립트를 클릭유도하여 유저의 정보 탈취 및 서버에 의도치않은 스크립트 요청
        // 기본적으로 세션을 사용하지 않는다는건 해당유저의 세션탈취(xss공격)에 안전
        // jwt를 쿠키에 담아 쿠키를 httponly또는 secure(https)처리하여 스크립트로는 서버에 요청 불가
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
