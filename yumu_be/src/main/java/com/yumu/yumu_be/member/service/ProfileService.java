package com.yumu.yumu_be.member.service;

import com.yumu.yumu_be.member.dto.PurchaseHistoryResponse;
import com.yumu.yumu_be.member.dto.ProfileRequest;
import com.yumu.yumu_be.member.dto.ProfileResponse;
import com.yumu.yumu_be.member.dto.SaleHistoryResponse;
import com.yumu.yumu_be.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProfileService {
    ProfileResponse getMyProfile(Member member);
    void updateMyProfile(ProfileRequest request, MultipartFile multipartFile, Member member) throws IOException;
    List<PurchaseHistoryResponse> getMyPurchaseHistory(Long cursor, int limit, Long memberId);
    List<SaleHistoryResponse> getMySaleHistory(Long cursor, int limit, Long memberId);
}