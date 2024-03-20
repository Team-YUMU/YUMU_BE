package com.yumu.yumu_be.member.service;

import com.yumu.yumu_be.exception.NotFoundException;
import com.yumu.yumu_be.image.S3Service;
import com.yumu.yumu_be.member.dto.PurchaseHistoryResponse;
import com.yumu.yumu_be.member.dto.ProfileRequest;
import com.yumu.yumu_be.member.dto.ProfileResponse;
import com.yumu.yumu_be.member.dto.SaleHistoryResponse;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.member.entity.PurchaseHistory;
import com.yumu.yumu_be.member.entity.SaleHistory;
import com.yumu.yumu_be.member.repository.MemberRepository;
import com.yumu.yumu_be.member.repository.PurchaseHistoryRepository;
import com.yumu.yumu_be.member.repository.SaleHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final SaleHistoryRepository saleHistoryRepository;

    @Override
    @Transactional
    public ProfileResponse getMyProfile(Member member) {
        Member myMember = isMember(member.getId());     //존재하는 멤버인지 확인
        return new ProfileResponse(myMember);
    }

    @Override
    @Transactional
    public void updateMyProfile(ProfileRequest request, MultipartFile multipartFile, Member member) throws IOException {
        Member myMember = isMember(member.getId()); //존재하는 멤버인지 확인
        String imageUrl = "";
        if (!multipartFile.isEmpty()) {             //이미지 수정하는지 확인
            imageUrl = s3Service.upload(multipartFile, String.valueOf(myMember.getId()));
        }
        myMember.updateProfile(request, imageUrl);
    }

    @Override
    public List<PurchaseHistoryResponse> getMyPurchaseHistory(Long cursor, int limit, Long memberId) {
        Slice<PurchaseHistory> history;
        PageRequest pageRequest = PageRequest.of(0, limit);
        if (cursor == null) {
            history = purchaseHistoryRepository.findTopByMember_IdOrderByIdDesc(memberId, pageRequest);
        } else {
            history = purchaseHistoryRepository.findNextPage(memberId, cursor, pageRequest);
        }

        List<PurchaseHistoryResponse> response = new ArrayList<>();

        for(PurchaseHistory h : history) {
            PurchaseHistoryResponse purchaseDto = PurchaseHistoryResponse.builder()
                    .artTitle(h.getArtTitle())
                    .artist(h.getArtist())
                    .price(h.getPrice())
                    .purchaseDate(h.getPurchaseDate())
                    .auctionId(h.getAuctionId())
                    .build();
            response.add(purchaseDto);
        }
        return response;
    }

    @Override
    public List<SaleHistoryResponse> getMySaleHistory(Long cursor, int limit, Long memberId) {
        Slice<SaleHistory> history;
        PageRequest pageRequest = PageRequest.of(0, limit);
        if (cursor == 0) {
            history = saleHistoryRepository.findTopByMember_IdOrderByIdDesc(memberId, pageRequest);
        } else {
            history = saleHistoryRepository.findNextPage(memberId, cursor, pageRequest);
        }

        List<SaleHistoryResponse> response = new ArrayList<>();

        for(SaleHistory h : history) {
            SaleHistoryResponse saleDto = SaleHistoryResponse.builder()
                    .artTitle(h.getArtTitle())
                    .artist(h.getArtist())
                    .price(h.getPrice())
                    .status(h.getStatus())
                    .saleDate(h.getSaleDate())
                    .auctionId(h.getAuctionId())
                    .build();
            response.add(saleDto);
        }
        return response;
    }

    //존재하는 멤버인지 확인하는 로직
    public Member isMember(Long id) {
        return memberRepository.findById(id).orElseThrow(NotFoundException.NotFoundMemberException::new);
    }

}
