package com.yumu.yumu_be.member.entity;

import com.yumu.yumu_be.art.entity.Status;
import com.yumu.yumu_be.auction.entity.Auction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class SaleHistory {
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
    private Status status;

    @NotNull
    private LocalDateTime saleDate;

    @NotNull
    private int memberId;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    public void updateStatus(Status status) {
        this.status = status;
    }
}
