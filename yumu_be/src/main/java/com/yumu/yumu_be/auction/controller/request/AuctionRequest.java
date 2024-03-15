package com.yumu.yumu_be.auction.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionRequest {
    private String artName;
    private String artImage;
    private String artDescription;
    private String artSize;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime artCreatedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime auctionStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime auctionEndDate;
    private int defaultBid;
    private String notice;
    private String receiveType;

    public AuctionRequest(String artName, String artImage, String artDescription, String artSize, LocalDateTime artCreatedDate, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, int defaultBid, String notice, String receiveType) {
        this.artName = artName;
        this.artImage = artImage;
        this.artDescription = artDescription;
        this.artSize = artSize;
        this.artCreatedDate = artCreatedDate;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.defaultBid = defaultBid;
        this.notice = notice;
        this.receiveType = receiveType;
    }
}
