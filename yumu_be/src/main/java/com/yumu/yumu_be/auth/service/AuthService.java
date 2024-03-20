package com.yumu.yumu_be.auth.service;

import com.yumu.yumu_be.auth.dto.LoginRequest;
import com.yumu.yumu_be.auth.dto.SignupRequest;
import com.yumu.yumu_be.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    CommonResponse signUp(SignupRequest signupRequest);
    CommonResponse logIn(LoginRequest loginRequest, HttpServletResponse response);

    CommonResponse logOut(HttpServletRequest request);

    CommonResponse checkNickname(String nickname);

    CommonResponse checkEmail(String email);

    String findPassword(String email);

    CommonResponse withdraw(String password, HttpServletRequest request);
}
