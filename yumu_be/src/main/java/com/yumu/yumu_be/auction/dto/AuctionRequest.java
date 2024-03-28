package com.yumu.yumu_be.auction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AuctionRequest {
    private String artName;
    private String artSubTitle;
    private String artDescription;
    private String artSummary;
    private String artSize;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime artCreatedDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSz")
    private LocalDateTime auctionStartDate;
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
