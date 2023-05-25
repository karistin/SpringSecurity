package com.lucas.springsecurity.service;

import com.lucas.springsecurity.domain.User;
import com.lucas.springsecurity.exception.AppException;
import com.lucas.springsecurity.exception.ErrorCode;
import com.lucas.springsecurity.repository.UserRepository;
import com.lucas.springsecurity.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.token.secret}")
    private String key;

    private final Long expireTime = 1000 * 60 * 60l;

    public String join(String userName, String password) {
        userRepository.findByUserName(userName).ifPresent(
            user -> {
                throw new AppException(ErrorCode.USERNAME_DUPLICATED
                , userName + " 이미 존재합니다.");
            }
        );
        User user = User.builder()
            .userName(userName)
            .password(passwordEncoder.encode(password))
            .build();
        userRepository.save(user);
        return "SUCCESS";
    }

    public String  login(String  username, String  password) {
//        not found User
        User user = userRepository.findByUserName(username)
            .orElseThrow(() ->
                new AppException(ErrorCode.USERNAME_NOT_FOUND, username + " 이 없습니다."));
        if (passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD, " 패스워드가 없습니다.");
        }

        String token = JwtTokenUtil.createToken(username, key, expireTime);

        return token;
    }
}
