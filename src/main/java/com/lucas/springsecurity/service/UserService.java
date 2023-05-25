package com.lucas.springsecurity.service;

import com.lucas.springsecurity.domain.User;
import com.lucas.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public String join(String userName, String password) {
        userRepository.findByUserName(userName).ifPresent(
            user -> {
                throw new RuntimeException(userName + "는 이미 존재합니다.");
            }
        );
        User user = User.builder()
            .userName(userName)
            .password(password)
            .build();
        userRepository.save(user);
        return "SUCCESS";
    }
}
