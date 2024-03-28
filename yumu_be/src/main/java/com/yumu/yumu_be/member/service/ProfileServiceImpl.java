package com.yumu.yumu_be.member.service;

import com.yumu.yumu_be.common.dto.CommonResponse;
import com.yumu.yumu_be.exception.BadRequestException;
import com.yumu.yumu_be.exception.NotFoundException;
import com.yumu.yumu_be.image.S3Service;
import com.yumu.yumu_be.member.dto.*;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.member.entity.PurchaseHistory;
import com.yumu.yumu_be.member.entity.SaleHistory;
import com.yumu.yumu_be.member.repository.MemberRepository;
import com.yumu.yumu_be.member.repository.PurchaseHistoryRepository;
import com.yumu.yumu_be.member.repository.SaleHistoryRepository;
import com.yumu.yumu_be.wishList.entity.WishList;
import com.yumu.yumu_be.wishList.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final MemberRepository memberRepository;
    private final S3Service s3Service;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final SaleHistoryRepository saleHistoryRepository;
    private final WishListRepository wishListRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile(Member member) {
        Member myMember = isMember(member.getId());     //존재하는 멤버인지 확인
        return new ProfileResponse(myMember);
    }

    @Override
    @Transactional
    public CommonResponse updateMyNickname(String nickname, Member member) {
        Member myMember = isMember(member.getId());
        //닉네임 중복 확인
        if (memberRepository.existsByNickname(nickname)) {
            throw new BadRequestException.DuplicatedNicknameException();
        }
        //닉네임 형식 확인
        if (!Pattern.matches("[a-zA-Z0-9가-힣]{2,10}", nickname)) {
            throw new BadRequestException.InvalidNicknameException();
        }
        myMember.updateNickname(nickname);
        return new CommonResponse("닉네임 수정 완료");
    }

    @Override
    @Transactional
    public CommonResponse updateMyIntroduce(String introduce, Member member) {
        Member myMember = isMember(member.getId());
        myMember.updateIntroduce(introduce);
        return new CommonResponse("소개글 수정 완료");
    }

    @Override
    @Transactional
    public CommonResponse updateMyProfileImage(MultipartFile profileImage, Member member) throws IOException{
        Member myMember = isMember(member.getId());
        String imageUrl = s3Service.upload(profileImage, String.valueOf(myMember.getId()));
        myMember.updateProfileImage(imageUrl);
        return new CommonResponse("이미지 수정 완료");
    }

    @Override
    @Transactional
    public CommonResponse deleteMyProfileImage(Long memberId) {
        Member member = isMember(memberId);
        s3Service.deleteFile(member.getProfileImage());
        member.updateProfileImage("https://yumu-image.s3.ap-northeast-2.amazonaws.com/default/octicon_person-24.jpg");
        return new CommonResponse("이미지 삭제 완료");
    }

    @Override
    @Transactional(readOnly = true)
    public CommonResponse checkMyPassword(String password, Member member) {
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadRequestException.NotMatchPasswordException();
        }
        return new CommonResponse("비밀번호 일치");
    }

    @Override
    @Transactional
    public CommonResponse updateMyPassword(PasswordRequest request, Long memberId) {
        Member myMember = isMember(memberId);
        if (!request.getNewPassword().equals(request.getNewCheckPassword())) {
            throw new BadRequestException.NotMatchPasswordException();
        }
        myMember.updatePassword(passwordEncoder.encode(request.getNewPassword()));

        return new CommonResponse("비밀번호 수정 완료");
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
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

    @Override
    @Transactional(readOnly = true)
    public List<WishResponse> getMyWishList(Long cursor, int limit, Long memberId) {
        Slice<WishList> wishs;
        PageRequest pageRequest = PageRequest.of(0, limit);
        if (cursor == 0) {
            wishs = wishListRepository.findTopByMember_IdOrderByIdDesc(memberId, pageRequest);
        } else {
            wishs = wishListRepository.findNextPage(memberId, cursor, pageRequest);
        }

        List<WishResponse> response = new ArrayList<>();

        for(WishList w : wishs) {
            WishResponse wishDto = WishResponse.builder()
                    .artTitle(w.getArt().getArtName())
                    .artSubtitle(w.getArt().getArtSubTitle())
                    .artist(w.getArt().getArtist())
                    .imageUrl(w.getArt().getArtImage())
                    .auctionId(w.getArt().getAuction().getId())
                    .build();
            response.add(wishDto);
        }
        return response;
    }

    //존재하는 멤버인지 확인하는 로직
    public Member isMember(Long id) {
        return memberRepository.findById(id).orElseThrow(NotFoundException.NotFoundMemberException::new);
    }

}
