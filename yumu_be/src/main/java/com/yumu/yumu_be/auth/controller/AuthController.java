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

import javax.swing.text.html.parser.Entity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signUp(@RequestBody @Valid SignupRequest signupRequest) throws Exception {
        return ResponseEntity.ok(authService.signUp(signupRequest));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> logIn(@RequestBody LoginRequest loginRequest, HttpServletResponse response) throws Exception {
        return ResponseEntity.ok(authService.logIn(loginRequest, response));
    }

    //로그아웃
    @GetMapping("/logout")
    public ResponseEntity<CommonResponse> logOut(HttpServletRequest request) {
        return ResponseEntity.ok(authService.logOut(request));
    }

    //카카오 로그인 및 회원가입
    @GetMapping("/kakao/callback")
    public ResponseEntity<CommonResponse> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return ResponseEntity.ok(kakaoService.kakaoLogin(code, response));
    }
}
