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
    private String price;
    private Status status;
    private LocalDateTime saleDate;
    private Long auctionId;
}
