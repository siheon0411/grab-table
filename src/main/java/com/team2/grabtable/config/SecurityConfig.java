package com.team2.grabtable.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 커스터마이징한 인증/인가 정책 동작
@RequiredArgsConstructor
public class SecurityConfig {

    private final OwnerDetailsService ownerDetailsService;

    @Bean
    public SecurityFilterChain filterChain(MyAuthenticationSuccessHandler successHandler,MyAuthenticationFailureHandler failureHandler,HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/", "/index.html", "/csrf-token", "/login", "/owner/register", "/register.html").permitAll()
                        .anyRequest().authenticated()
                )
//              .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email") //클라이언트에서 보낼 이름이 email
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                )
                .logout(logout -> logout.permitAll())
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 이메일/비밀번호가 맞는지 검증(커스텀 로그인 API에서 직접 인증 처리)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(ownerDetailsService) // 사용자를 이메일로 조회하는 UserDetailService 등록
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }

}
