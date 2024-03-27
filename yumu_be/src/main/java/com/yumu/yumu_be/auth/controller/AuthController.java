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
import org.springframework.web.servlet.view.RedirectView;

import javax.swing.text.html.parser.Entity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signUp(@RequestBody @Valid SignupRequest signupRequest) {
        return ResponseEntity.ok(authService.signUp(signupRequest));
    }

    //닉네임 중복 확인
    @GetMapping("/signup/nickname-check")
    public ResponseEntity<CommonResponse> checkNickname(@RequestBody String nickname) {
        return ResponseEntity.ok(authService.checkNickname(nickname));
    }

    //이메일 중복 확인
    @GetMapping("/signup/email-check")
    public ResponseEntity<CommonResponse> checkEmail(@RequestBody String email) {
        return ResponseEntity.ok(authService.checkEmail(email));
    }


    //로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponse> logIn(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.logIn(loginRequest, response));
    }

    //비밀번호 찾기 - 임시 비밀번호 발급
    @GetMapping("/login/find-password")
    public ResponseEntity<String> findPassword(@RequestBody String email, HttpServletResponse response) {
        return ResponseEntity.ok(authService.findPassword(email));
    }

    //로그아웃
    @GetMapping("/logout")
    public ResponseEntity<CommonResponse> logOut(HttpServletRequest request) {
        return ResponseEntity.ok(authService.logOut(request));
    }

    //탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<CommonResponse> withdraw(@RequestBody String password, HttpServletRequest request) {
        return ResponseEntity.ok(authService.withdraw(password, request));
    }

    //카카오 로그인 및 회원가입
    @GetMapping("/kakao/callback")
    public ResponseEntity<CommonResponse> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return ResponseEntity.ok(kakaoService.kakaoLogin(code, response));
    }
}
