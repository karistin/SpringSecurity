package com.lucas.springsecurity.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lucas.springsecurity.dto.UserJoinRequest;
import com.lucas.springsecurity.dto.UserLoginRequest;
import com.lucas.springsecurity.exception.AppException;
import com.lucas.springsecurity.exception.ErrorCode;
import com.lucas.springsecurity.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    @WithMockUser
    void join() throws Exception {
        String name = "ksj";
        String password = "1234";

        mockMvc.perform(post("/api/v1/users/join")
                .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(new UserJoinRequest(name, password))))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패")
    @WithMockUser
    void joinFail() throws Exception {
        String name = "ksj";
        String password = "1234";
        when(userService.join(any(), any())).thenThrow(
            new AppException(ErrorCode.USERNAME_DUPLICATED, "해당 ID가 존재합니다.")
        );
        mockMvc.perform(post("/api/v1/users/join")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserJoinRequest(name, password))))
            .andDo(print())
            .andExpect(status().isConflict());
    }


    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_Success() throws Exception {
        String name = "ksj";
        String password = "1234";
        when(userService.login(any(), any())).thenReturn("token");
        mockMvc.perform(post("/api/v1/users/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(name, password))))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 실패 - Not found User Name")
    @WithMockUser
    void login_fail1() throws Exception {
        String name = "ksj";
        String password = "1234";
        when(userService.login(any(), any()))
            .thenThrow(new AppException(ErrorCode.USERNAME_NOT_FOUND, ""));

        mockMvc.perform(post("/api/v1/users/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(name, password))))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 실패 - Password error")
    @WithMockUser
    void login_fail2() throws Exception {
        String name = "ksj";
        String password = "1234";
        when(userService.login(any(), any()))
            .thenThrow(new AppException(ErrorCode.INVALID_PASSWORD, ""));

        mockMvc.perform(post("/api/v1/users/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new UserLoginRequest(name, password))))
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }
}