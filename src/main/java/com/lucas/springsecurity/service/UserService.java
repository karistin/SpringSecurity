package com.lucas.springsecurity.service;

import com.lucas.springsecurity.domain.User;
import com.lucas.springsecurity.exception.AppException;
import com.lucas.springsecurity.exception.ErrorCode;
import com.lucas.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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
}
