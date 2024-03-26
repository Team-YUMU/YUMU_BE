package com.yumu.yumu_be.member.entity;

import com.yumu.yumu_be.art.entity.Art;
import com.yumu.yumu_be.auction.entity.Auction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor
public class PurchaseHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String artTitle;

    @NotNull
    private String artist;

    @NotNull
    private Long price;

    @NotNull
    private String artImage;

    @NotNull
    private LocalDateTime purchaseDate;

    private int auctionId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "member_id")
    private Member member;

    public PurchaseHistory(Art art, Long price) {
        this. artTitle = art.getArtName();
        this.artist = art.getArtist();
        this.price = price;
        this.artImage = art.getArtImage();
        this.purchaseDate = LocalDateTime.now();
        this.auctionId = art.getAuction().getId();
    }
}
