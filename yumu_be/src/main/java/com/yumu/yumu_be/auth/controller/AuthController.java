package com.yumu.yumu_be.auth.controller;

import com.yumu.yumu_be.auth.dto.LoginRequest;
import com.yumu.yumu_be.auth.dto.SignupRequest;
import com.yumu.yumu_be.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    //회원가입
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody @Valid SignupRequest signupRequst) throws Exception {
        authService.signUp(signupRequst);
    }

    //로그인
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public void logIn(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws Exception {
        authService.logIn(loginRequest, response);
    }

    //로그아웃
    @GetMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logOut(HttpServletRequest request) {
        authService.logOut(request);
    }
}
