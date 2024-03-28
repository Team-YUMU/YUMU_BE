package com.yumu.yumu_be.member.controller;

import com.yumu.yumu_be.common.dto.CommonResponse;
import com.yumu.yumu_be.member.dto.*;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.member.service.ProfileService;
import com.yumu.yumu_be.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

    //내 정보 닉네임 수정
    @PutMapping(value = "/member/nickname")
    public ResponseEntity<CommonResponse> updateMyNickname(@RequestParam(value = "nickname") String nickname, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(profileService.updateMyNickname(nickname, userDetails.getMember()));
    }

    //내 정보 소개글 수정
    @PutMapping(value = "/member/introduce")
    public ResponseEntity<CommonResponse> updateMyIntroduce(@RequestParam(value = "introduce") String introduce, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(profileService.updateMyIntroduce(introduce, userDetails.getMember()));
    }

    //내 정보 이미지 수정
    @PutMapping(value = "/member/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse> updateMyProfileImage(@RequestParam(value = "profileImage") MultipartFile profileImage, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return ResponseEntity.ok(profileService.updateMyProfileImage(profileImage, userDetails.getMember()));
    }

    //내 정보 이미지 삭제
    @DeleteMapping("/member/profile-image")
    public ResponseEntity<CommonResponse> deleteMyProfileImage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(profileService.deleteMyProfileImage(userDetails.getMember().getId()));
    }

    //비밀번호 일치 체크
    @GetMapping("/member/password")
    public ResponseEntity<CommonResponse> checkMyPassword(String password, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(profileService.checkMyPassword(password, userDetails.getMember()));
    }

    //내 비밀번호 수정
    @PutMapping("/member/password")
    public ResponseEntity<CommonResponse> updateMyPassword(@RequestBody PasswordRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(profileService.updateMyPassword(request, userDetails.getMember().getId()));
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
