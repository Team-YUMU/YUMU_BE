package com.yumu.yumu_be.member.dto;

import com.yumu.yumu_be.art.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SaleHistoryResponse {
    private String artTitle;
    private String artist;
    private Long price;
    private LocalDateTime saleDate;
    private int auctionId;
}
