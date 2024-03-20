package com.yumu.yumu_be.auction.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AuctionResponse {
    private int page;
    private long totalElements;
    private int totalPages;
    private List<AuctionDto> auctions;

    public AuctionResponse(int page, long totalElements, int totalPages, List<AuctionDto> auctions) {
        this.page = page;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.auctions = auctions;
    }

    public static AuctionResponse of(int page, long totalElements,int totalPages, List<AuctionDto> auctions) {
        return new AuctionResponse(page, totalElements, totalPages, auctions);
    }
}
