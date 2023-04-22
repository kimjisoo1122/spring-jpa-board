package com.example.shop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
//@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


//    @Value("${jwt.secret}")
//    private String secretKey;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
//                .httpBasic().disable() // 기존 http보안 프로토콜을  쓸거냐? 취약하므로 https사용권장
//                .csrf().disable() // cross-site request forgery 공격방지기능 사용하지 않는다
//                // jwt를 사용할 경우 보통 사용하지 않는다
//                .cors().and() // cross-origin-resource-sharing를 사용
//                // 웹 애플리케이션에서 다른 도메인의 리소스에 접근할 수 있도록 해주는 보안 메커니즘
//                .authorizeRequests()
//                .antMatchers("/login/**", "/").permitAll()
//                .antMatchers("/board/**").authenticated()
//                .and()
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .addFilterBefore(new JwtFilter(secretKey), UsernamePasswordAuthenticationFilter.class)
//                .build();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.formLogin()
//                .loginPage("/login").permitAll()
////                .defaultSuccessUrl("/")
////                .usernameParameter("email")
////                .failureUrl("/login")
//                .and()
//                .logout().permitAll();
//        http.authorizeRequests()
////                .mvcMatchers("/", "/login/**").permitAll()
//                .mvcMatchers("/admin/**").hasRole("ADMIN")
//                .anyRequest().authenticated();
//
//        return http.build();
//    }
//
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
