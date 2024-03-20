package com.yumu.yumu_be.auction.dto;

import com.yumu.yumu_be.auction.entity.Auction;
import com.yumu.yumu_be.auction.entity.Receive;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionDetailDto {
    private AuctionDto artInfo;
    private int id;
    private String artDescription;
    private String artSize;
    private LocalDateTime artCreatedDate;
    private LocalDateTime auctionStartDate;
    private LocalDateTime auctionEndDate;
    private int defaultBid;
    private int winningBid;
    private String winningBidder;
    private String notice;
    private Receive receiveType;

    public AuctionDetailDto(AuctionDto artInfo, int id, String artDescription, String artSize, LocalDateTime artCreatedDate, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, int defaultBid, int winningBid, String winningBidder, String notice, Receive receiveType) {
        this.artInfo = artInfo;
        this.id = id;
        this.artDescription = artDescription;
        this.artSize = artSize;
        this.artCreatedDate = artCreatedDate;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.defaultBid = defaultBid;
        this.winningBid = winningBid;
        this.winningBidder = winningBidder;
        this.notice = notice;
        this.receiveType = receiveType;
    }

    public static AuctionDetailDto of(Auction auction) {
        return new AuctionDetailDto(AuctionDto.of(auction.getArt()), auction.getId(), auction.getArtDescription(), auction.getArtSize(), auction.getArtCreatedDate(), auction.getAuctionStartDate(), auction.getAuctionEndDate(), auction.getDefaultBid(), auction.getWinningBid(), auction.getWinningBidder(), auction.getNotice(), auction.getReceiveType());
    }
}
