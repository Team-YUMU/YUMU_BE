package com.yumu.yumu_be.wishList.service;

import com.yumu.yumu_be.art.repository.ArtRepository;
import com.yumu.yumu_be.art.repository.domain.Art;
import com.yumu.yumu_be.exception.NotFoundException;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.member.repository.MemberRepository;
import com.yumu.yumu_be.wishList.entity.WishList;
import com.yumu.yumu_be.wishList.repository.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WishListService {

    private final WishListRepository wishListRepository;
    private final MemberRepository memberRepository;
    private final ArtRepository artRepository;

    public WishListService(WishListRepository wishListRepository, MemberRepository memberRepository, ArtRepository artRepository) {
        this.wishListRepository = wishListRepository;
        this.memberRepository = memberRepository;
        this.artRepository = artRepository;
    }

    @Transactional
    public String addWishList(String memberId, int artId) {
        Member member = memberRepository.findByNickname(memberId).orElseThrow(NotFoundException.NotFoundMemberException::new);
        Art art = artRepository.findById(artId).orElseThrow(NotFoundException.NotFoundMemberException::new);
        if (!wishListRepository.existsByArtIdAndMemberId(art.getId(), member.getId())) {
            wishListRepository.save(WishList.of(member, art));
            return "해당 작품이 찜 리스트에 추가되었습니다.";
        }
        WishList wishList = wishListRepository.findByArtIdAndMemberId(art.getId(), member.getId()).orElseThrow(NotFoundException.NotFoundMemberException::new);
        wishListRepository.deleteById(wishList.getId());
        return "해당 작품이 찜 리스트에서 삭제되었습니다.";
    }
}
