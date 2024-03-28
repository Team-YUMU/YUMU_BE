package com.yumu.yumu_be.bid.service;

import com.yumu.yumu_be.bid.dto.HighestPriceResponse;
import com.yumu.yumu_be.common.dto.CommonResponse;
import com.yumu.yumu_be.member.entity.Member;


public interface BidService {
    CommonResponse bid(int auctionId, Double price, Long memberId);
    CommonResponse successBid(int auctionId, Member member);
    HighestPriceResponse firstHighestPrice(int auctionId);
}
