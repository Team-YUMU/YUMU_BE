package com.yumu.yumu_be.pay.controller;

import com.yumu.yumu_be.pay.dto.KakaoPayApproveResponse;
import com.yumu.yumu_be.pay.dto.KakaoPayReadyResponse;
import com.yumu.yumu_be.pay.dto.KakaoPayRequest;
import com.yumu.yumu_be.pay.service.KakaoPayService;
import com.yumu.yumu_be.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/kakaopay")
public class KakaopayController {

    private final KakaoPayService kakaoPayService;

    //결제 요청
    @PostMapping("/ready")
    public ResponseEntity<KakaoPayReadyResponse> readyKakaoPay(@RequestBody KakaoPayRequest kakaoPayRequest, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return ResponseEntity.ok(kakaoPayService.kakaoPayReady(kakaoPayRequest, userDetails.getMember(), request));
    }

    //결제 승인 요청
    @GetMapping("/success")
    public ResponseEntity<KakaoPayApproveResponse> successKakaoPay(@RequestParam("pg_token") String pgToken, HttpServletRequest request) {
        return ResponseEntity.ok(kakaoPayService.kakaoPayApprove(pgToken, request));
    }

    //결제 진행 중 취소
    @GetMapping("/cancel")
    public RedirectView cancel() {
        return new RedirectView("http://localhost:3000/mypage");
    }

    //결제 실패
    @GetMapping("/fail")
    public RedirectView fail() {
        return new RedirectView("http://localhost:3000/mypage");
    }

}
