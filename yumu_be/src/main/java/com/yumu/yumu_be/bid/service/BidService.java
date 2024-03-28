package com.yumu.yumu_be.bid.service;

import com.yumu.yumu_be.bid.dto.HighestPriceResponse;
import com.yumu.yumu_be.common.dto.CommonResponse;


public interface BidService {
    CommonResponse bid(int auctionId, Double price, Long memberId);
    CommonResponse successBid(int auctionId);
    HighestPriceResponse firstHighestPrice(int auctionId);
}
