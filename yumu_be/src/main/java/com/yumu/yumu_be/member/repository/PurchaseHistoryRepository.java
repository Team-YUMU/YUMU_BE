package com.yumu.yumu_be.member.repository;

import com.yumu.yumu_be.member.entity.PurchaseHistory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistory, Long> {
    Slice<PurchaseHistory> findTopByMember_IdOrderByIdDesc(Long memberId, PageRequest pageRequest);
    @Query("SELECT a FROM PurchaseHistory a WHERE a.member.id = :memberId and a.id <= :cursor ORDER BY a.id DESC")
    Slice<PurchaseHistory> findNextPage(@Param("memberId") Long memberId, @Param("cursor") Long cursor, PageRequest pageRequest);
}
