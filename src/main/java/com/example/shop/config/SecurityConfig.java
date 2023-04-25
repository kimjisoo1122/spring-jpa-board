package com.example.shop.config;

import com.example.shop.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .antMatchers("/member/sign", "/").permitAll()
                .antMatchers(HttpMethod.POST, "/login", "member/sign").authenticated()
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
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies(JwtUtil.ACCESS_TOKEN)
                .and()
                .authenticationProvider(new CustomAuthenticationProvider(customUserDetailService, passwordEncoder()))
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                // jwt를 사용하여 인증을 jwt에 위임 세션상태를 유지하지 않음
                // cross site request forgery는 사용자의 세션을 탈취하여 공격하기에
                // 세션을 사용하지 않으면 공격에 안전
                .addFilterBefore(new JwtFilter(customUserDetailService), UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 스프링시큐리티는 기본적으로 get을 제외한 http요청에 csrf토큰을 주고 받아 보안함
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
