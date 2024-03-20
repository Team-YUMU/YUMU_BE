package com.yumu.yumu_be.wishList.repository;

import com.yumu.yumu_be.wishList.entity.WishList;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Integer> {

    boolean existsByArtIdAndMemberId(int artId, long memberId);

    Optional<WishList> findByArtIdAndMemberId(int artId, long memberId);

    Slice<WishList> findTopByMember_IdOrderByIdDesc(Long memberId, PageRequest pageRequest);

    @Query("SELECT a FROM WishList a WHERE a.member.id = :memberId and a.id <= :cursor ORDER BY a.id DESC")
    Slice<WishList> findNextPage(@Param("memberId") Long memberId, @Param("cursor") Long cursor, PageRequest pageRequest);

}
