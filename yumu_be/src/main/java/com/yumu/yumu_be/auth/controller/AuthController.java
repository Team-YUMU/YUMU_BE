package com.yumu.yumu_be.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yumu.yumu_be.auth.dto.*;
import com.yumu.yumu_be.auth.service.AuthService;
import com.yumu.yumu_be.auth.service.KakaoService;
import com.yumu.yumu_be.common.dto.CommonResponse;
import com.yumu.yumu_be.member.dto.PasswordRequest;
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
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;
    private final KakaoService kakaoService;

    //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<CommonResponse> signUp(@RequestBody @Valid SignupRequest signupRequest) {
        return ResponseEntity.ok(authService.signUp(signupRequest));
    }

    //닉네임 중복 확인
    @GetMapping("/auth/signup/nickname-check")
    public ResponseEntity<CommonResponse> checkNickname(@RequestParam(value = "nickname") String nickname) {
        return ResponseEntity.ok(authService.checkNickname(nickname));
    }

    //이메일 중복 확인
    @GetMapping("/auth/signup/email-check")
    public ResponseEntity<CommonResponse> checkEmail(@RequestParam(value = "email") String email) {
        return ResponseEntity.ok(authService.checkEmail(email));
    }


    //로그인
    @PostMapping("/auth/login")
    public ResponseEntity<CommonResponse> logIn(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.logIn(loginRequest, response));
    }

    //비밀번호 찾기 - 임시 비밀번호 발급
    @GetMapping("/auth/login/find-password")
    public ResponseEntity<String> findPassword(@RequestBody PasswordFindRequest request) {
        return ResponseEntity.ok(authService.findPassword(request.getEmail()));
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse> logOut(HttpServletRequest request) {
        return ResponseEntity.ok(authService.logOut(request));
    }

    //탈퇴
    @DeleteMapping("/withdraw")
    public ResponseEntity<CommonResponse> withdraw(@RequestBody WithdrawRequest withdrawRequest, HttpServletRequest request) {
        return ResponseEntity.ok(authService.withdraw(withdrawRequest.getPassword(), request));
    }

    //카카오 로그인 및 회원가입
    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<CommonResponse> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        return ResponseEntity.ok(kakaoService.kakaoLogin(code, response));
    }
}
