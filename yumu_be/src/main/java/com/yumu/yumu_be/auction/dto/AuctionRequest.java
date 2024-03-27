package com.yumu.yumu_be.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuctionRequest {
    private String artName;
    private String artSubTitle;
    private String artDescription;
    private String artSummary;
    private String artSize;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private LocalDateTime artCreatedDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private LocalDateTime auctionStartDate;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private LocalDateTime auctionEndDate;
    private Integer defaultBid;
    private String notice;
    private String receiveType;
    private MultipartFile image;

    public AuctionRequest(String artName, String artSubTitle, String artDescription, String artSummary, String artSize, LocalDateTime artCreatedDate, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, Integer defaultBid, String notice, String receiveType, MultipartFile image) {
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
