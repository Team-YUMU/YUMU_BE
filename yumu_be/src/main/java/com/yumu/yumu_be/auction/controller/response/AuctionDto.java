package com.yumu.yumu_be.auction.controller.response;

import com.yumu.yumu_be.art.repository.domain.Art;
import com.yumu.yumu_be.art.repository.domain.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionDto {
    private String artName;
    private String artImage;
    private String artist;
    private String status;
    private LocalDateTime createdAt;
    private int wishCnt;

    public AuctionDto(String artName, String artImage, String artist, String status, LocalDateTime createdAt, int wishCnt) {
        this.artName = artName;
        this.artImage = artImage;
        this.artist = artist;
        this.status = status;
        this.createdAt = createdAt;
        this.wishCnt = wishCnt;
    }

    public static AuctionDto of(Art art) {
        return new AuctionDto(art.getArtName(), art.getArtImage(), art.getArtist(), Status.of(art.getStatus()), art.getCreatedAt(), art.getWishCnt());
    }
}
