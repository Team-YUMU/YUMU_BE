package com.yumu.yumu_be.auth.service;

import com.yumu.yumu_be.auth.dto.LoginRequest;
import com.yumu.yumu_be.auth.dto.SignupRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    void signUp(SignupRequest signupRequst) throws Exception;
    void logIn(LoginRequest loginRequest, HttpServletResponse response) throws Exception;

    void logOut(HttpServletRequest request);
}
