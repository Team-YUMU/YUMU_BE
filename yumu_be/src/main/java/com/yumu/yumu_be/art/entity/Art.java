package com.yumu.yumu_be.art.entity;

import com.yumu.yumu_be.auction.entity.Auction;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Art {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="art_id")
    private int id;
    private String artName;
    private String artSubTitle;
    private String artImage;
    private String artist;
    private Status status;
    private LocalDateTime createdAt;
    private int wishCnt;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id", referencedColumnName = "auction_id")
    private Auction auction;

    public Art() {
    }

    public Art(String artName, String artSubTitle, String artImage, String artist, Auction auction) {
        this.artName = artName;
        this.artSubTitle = artSubTitle;
        this.artImage = artImage;
        this.artist = artist;
        this.status = Status.ON;
        this.createdAt = LocalDateTime.now();
        this.wishCnt = 0;
        this.auction = auction;
    }

    public static Art of(String artName, String artSubTitle, String artImage, String artist, Auction auction) {
        Art art = new Art(artName, artSubTitle, artImage, artist, auction);
        auction.addArt(art);
        return art;
    }

    public void updateTo(String artName, String artSubTitle, String artImage, String artist, Auction auction) {
        this.artName = artName;
        this.artSubTitle = artSubTitle;
        this.artImage = artImage;
        this.artist = artist;
        this.auction = auction;
        auction.addArt(this);
    }

    public void increaseWishCnt() {
        this.wishCnt += 1;
    }

    public void decreaseWishCnt() {
        if (this.wishCnt > 0) {
            this.wishCnt -= 1;
        }
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
