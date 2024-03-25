package com.yumu.yumu_be.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HighestPriceResponse {
    private int auctionId;
    private String bidder;
    private Long price;
}
