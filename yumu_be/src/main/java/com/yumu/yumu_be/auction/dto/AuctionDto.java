package com.yumu.yumu_be.auction.dto;

import com.yumu.yumu_be.art.entity.Art;
import com.yumu.yumu_be.art.entity.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionDto {
    private int id;
    private String artName;
    private String artSubTitle;
    private String artImage;
    private String artist;
    private String status;
    private LocalDateTime createdAt;
    private int wishCnt;

    public AuctionDto(int id, String artName, String artSubTitle, String artImage, String artist, String status, LocalDateTime createdAt, int wishCnt) {
        this.id = id;
        this.artName = artName;
        this.artSubTitle = artSubTitle;
        this.artImage = artImage;
        this.artist = artist;
        this.status = status;
        this.createdAt = createdAt;
        this.wishCnt = wishCnt;
    }

    public static AuctionDto of(Art art) {
        return new AuctionDto(art.getId(), art.getArtName(), art.getArtSubTitle(), art.getArtImage(), art.getArtist(), Status.of(art.getStatus()), art.getCreatedAt(), art.getWishCnt());
    }
}
