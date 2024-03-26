package com.yumu.yumu_be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PurchaseHistoryResponse {
    private String artTitle;
    private String artist;
    private Long price;
    private LocalDateTime purchaseDate;
    private int auctionId;
}
