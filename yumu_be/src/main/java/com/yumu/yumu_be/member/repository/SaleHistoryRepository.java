package com.yumu.yumu_be.member.repository;

import com.yumu.yumu_be.member.entity.SaleHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleHistoryRepository extends JpaRepository<SaleHistory, Long> {
    Slice<SaleHistory> findTopByMemberIdOrderByIdDesc(Long memberId, PageRequest pageRequest);

    @Query("SELECT a FROM SaleHistory a WHERE a.member.id = :memberId and a.id <= :cursor ORDER BY a.id DESC")
    Slice<SaleHistory> findNextPage(@Param("memberId") Long memberId, @Param("cursor") Long cursor, PageRequest pageRequest);

    SaleHistory findByAuctionId(int auctionId);

    List<SaleHistory> findAllByMemberId(Long memberId);
}
