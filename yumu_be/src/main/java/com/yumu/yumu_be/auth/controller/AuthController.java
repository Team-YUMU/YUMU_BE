package com.yumu.yumu_be.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yumu.yumu_be.auth.dto.LoginRequest;
import com.yumu.yumu_be.auth.dto.SignupRequest;
import com.yumu.yumu_be.auth.service.AuthService;
import com.yumu.yumu_be.auth.service.KakaoService;
import com.yumu.yumu_be.common.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;

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

    //카카오 로그인 및 회원가입
    @GetMapping("/kakao/callback")
    public ResponseEntity<CommonResponse> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return ResponseEntity.ok(kakaoService.kakaoLogin(code, response));
    }
}
