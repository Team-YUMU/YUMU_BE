package com.yumu.yumu_be.member.controller;

import com.yumu.yumu_be.member.dto.*;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.member.service.ProfileService;
import com.yumu.yumu_be.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProfileController {

    private final ProfileService profileService;

    //내 정보 조회
    @GetMapping("/member")
    public ResponseEntity<ProfileResponse> getMyProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        return ResponseEntity.ok(profileService.getMyProfile(member));
    }

    //내 정보 수정
    @PutMapping("/member")
    public ResponseEntity<String> updateMyProfile(@RequestPart("request") ProfileRequest request, @RequestPart(value = "image", required = false) MultipartFile multipartFile, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        profileService.updateMyProfile(request, multipartFile, userDetails.getMember());
        return ResponseEntity.ok("내 정보 수정 완료");
    }

    //내 구매 내역 조회
    @GetMapping("/my-page/buy")
    public ResponseEntity<List<PurchaseHistoryResponse>> getMyPurChaseHistory(@RequestParam(value = "cursor") Long cursor, @RequestParam(value = "limit") int limit, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long memberId = userDetails.getMember().getId();
        return ResponseEntity.ok(profileService.getMyPurchaseHistory(cursor, limit, memberId));
    }

    //내 판매 내역 조회
    @GetMapping("/my-page/sold")
    public ResponseEntity<List<SaleHistoryResponse>> getMySaleHistory(@RequestParam(value = "cursor") Long cursor, @RequestParam(value = "limit") int limit, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long memberId = userDetails.getMember().getId();
        return ResponseEntity.ok(profileService.getMySaleHistory(cursor, limit, memberId));
    }

    //내 찜 내역 조회
    @GetMapping("/my-page/wish")
    public ResponseEntity<List<WishResponse>> getMyWishList(@RequestParam(value = "cursor") Long cursor, @RequestParam(value = "limit") int limit, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long memberId = userDetails.getMember().getId();
        return ResponseEntity.ok(profileService.getMyWishList(cursor, limit, memberId));
    }
}
