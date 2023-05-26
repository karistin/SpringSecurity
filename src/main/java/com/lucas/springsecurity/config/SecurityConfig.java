package com.lucas.springsecurity.config;

import com.lucas.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final UserService userService;

    @Value(value = "${jwt.token.secret}")
    private String secretKey;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
//            UI disable
            .httpBasic().disable()
//            crosssite
            .csrf().disable()
            .cors().and()
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/users/join", "api/v1/users/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
//            인증이 필요한 패턴
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // 스프링시큐리티가 생성하지도않고 기존것을 사용하지도 않음
            .and()
            .addFilterBefore(new JwtFilter(userService, secretKey),
                UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
