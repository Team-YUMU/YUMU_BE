package com.yumu.yumu_be.member.service;

import com.yumu.yumu_be.common.dto.CommonResponse;
import com.yumu.yumu_be.member.dto.*;
import com.yumu.yumu_be.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProfileService {
    ProfileResponse getMyProfile(Member member);
    List<PurchaseHistoryResponse> getMyPurchaseHistory(Long cursor, int limit, Long memberId);
    List<SaleHistoryResponse> getMySaleHistory(Long cursor, int limit, Long memberId);
    List<WishResponse> getMyWishList(Long cursor, int limit, Long memberId);
    CommonResponse updateMyNickname(String nickname, Member member);
    CommonResponse updateMyIntroduce(String introduce, Member member);
    CommonResponse updateMyProfileImage(MultipartFile profileImage, Member member) throws IOException;
    CommonResponse deleteMyProfileImage(Long memberId);
    CommonResponse updateMyPassword(PasswordRequest request, Long memberId);
}
