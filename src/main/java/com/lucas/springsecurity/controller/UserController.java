package com.lucas.springsecurity.controller;


import com.lucas.springsecurity.dto.UserJoinRequest;
import com.lucas.springsecurity.dto.UserLoginRequest;
import com.lucas.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody UserJoinRequest userJoinRequest) {
        userService.join(userJoinRequest.getUsername(), userJoinRequest.getPassword());
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest userLoginRequest) {
        String token = userService.login(userLoginRequest.getUsername(),
            userLoginRequest.getPassword());
        return ResponseEntity.ok().body(token);
    }
}
