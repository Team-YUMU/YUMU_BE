package com.yumu.yumu_be.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
public class AuctionRequest {
    private String artName;
    private String artSubTitle;
    private String artDescription;
    private String artSummary;
    private String artSize;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime artCreatedDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime auctionStartDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime auctionEndDate;
    private Integer defaultBid;
    private String notice;
    private String receiveType;
    private MultipartFile image;

    public AuctionRequest(String artName, String artSubTitle, String artDescription, String artSummary, String artSize, LocalDateTime artCreatedDate, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, int defaultBid, String notice, String receiveType, MultipartFile image) {
        this.artName = artName;
        this.artSubTitle = artSubTitle;
        this.artDescription = artDescription;
        this.artSummary = artSummary;
        this.artSize = artSize;
        this.artCreatedDate = artCreatedDate;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.defaultBid = defaultBid;
        this.notice = notice;
        this.receiveType = receiveType;
        this.image = image;
    }
}
