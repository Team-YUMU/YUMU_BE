package com.yumu.yumu_be.auction.repository;

import com.yumu.yumu_be.auction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionRepository extends JpaRepository<Auction, Integer> {
}
