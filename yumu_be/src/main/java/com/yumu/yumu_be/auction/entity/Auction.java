package com.yumu.yumu_be.auction.entity;

import com.yumu.yumu_be.art.entity.Art;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Auction {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="auction_id")
    private int id;
    @Column(columnDefinition = "TEXT")
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
    @OneToOne(mappedBy = "auction", cascade = CascadeType.ALL, orphanRemoval = true)
    private Art art;

    public Auction() {
    }

    public Auction(String artDescription, String artSize, LocalDateTime artCreatedDate, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, int defaultBid, String notice, Receive receiveType) {
        this.artDescription = artDescription;
        this.artSize = artSize;
        this.artCreatedDate = artCreatedDate;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.defaultBid = defaultBid;
        this.winningBid = 0;
        this.notice = notice;
        this.receiveType = receiveType;
    }

    public static Auction of(String artDescription, String artSize, LocalDateTime artCreatedDate, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, int defaultBid, String notice, String receiveType) {
        return new Auction(artDescription, artSize, artCreatedDate, auctionStartDate, auctionEndDate, defaultBid, notice, Receive.of(receiveType));
    }

    public void addArt(Art art) {
        this.art = art;
    }

    public void updateTo(String artDescription, String artSize, LocalDateTime artCreatedDate, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, int defaultBid, String notice, String receiveType) {
        this.artDescription = artDescription;
        this.artSize = artSize;
        this.artCreatedDate = artCreatedDate;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.defaultBid = defaultBid;
        this.winningBid = 0;
        this.notice = notice;
        this.receiveType = Receive.of(receiveType);
    }
}
