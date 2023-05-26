package com.lucas.springsecurity.config;

import com.lucas.springsecurity.service.UserService;
import com.lucas.springsecurity.utils.JwtTokenUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String sercetKey;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

//        username에서 Token를 가져온다.
        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorization : " + authorization);

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.error("authorization이 잘못되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }

//        토큰 가져오기
        String token = authorization.split(" ")[1];

        if (JwtTokenUtil.isExpired(token, sercetKey)) {
            log.error("토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        String userName = JwtTokenUtil.getUserName(token, sercetKey);

//        권한 부여 (DB .. )
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(userName, null,
                List.of(new SimpleGrantedAuthority("USER")));

//        Detail build => 올바른 가?
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//        도장 쾅
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//        이 request는 인증된 요청입니다.
        filterChain.doFilter(request, response);

    }
}
