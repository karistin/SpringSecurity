package com.lucas.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
//            UI disable
            .httpBasic().disable()
            .csrf().disable()
            .cors().and()
            .authorizeHttpRequests()
            .requestMatchers("/api/**").permitAll()
            .requestMatchers("/api/v1/users/join", "api/v1/users/login").permitAll()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            // 스프링시큐리티가 생성하지도않고 기존것을 사용하지도 않음
            .and()
            .build();
    }
}
