package com.yumu.yumu_be.wishList.repository;

import com.yumu.yumu_be.wishList.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList, Integer> {

    boolean existsByArtIdAndMemberId(int artId, long memberId);

    Optional<WishList> findByArtIdAndMemberId(int artId, long memberId);

}
