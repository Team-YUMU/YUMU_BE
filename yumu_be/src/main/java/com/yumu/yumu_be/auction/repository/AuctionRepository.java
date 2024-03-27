package com.yumu.yumu_be.auction.repository;

import com.yumu.yumu_be.auction.entity.Auction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {

    @Query(value = "select * from auction as a where Date_Format(a.auction_start_date,'%Y-%m-%d %h:%i:%s') > Date_Format(:now,'%Y-%m-%d %h:%i:%s')" , nativeQuery = true)
    Page<Auction> findLiveSoon(Pageable pageable, @Param("now") LocalDateTime now);
}
