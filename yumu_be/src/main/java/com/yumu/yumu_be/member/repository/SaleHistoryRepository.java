package com.yumu.yumu_be.member.repository;

import com.yumu.yumu_be.member.entity.SaleHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaleHistoryRepository extends JpaRepository<SaleHistory, Long> {
    Slice<SaleHistory> findTopByMember_IdOrderByIdDesc(Long memberId, PageRequest pageRequest);

    @Query("SELECT a FROM SaleHistory a WHERE a.member.id = :memberId and a.id <= :cursor ORDER BY a.id DESC")
    Slice<SaleHistory> findNextPage(@Param("memberId") Long memberId, @Param("cursor") Long cursor, PageRequest pageRequest);
}
