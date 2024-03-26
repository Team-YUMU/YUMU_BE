package com.yumu.yumu_be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WishResponse {
    private String artTitle;
    private String artSubtitle;
    private String artist;
    private String imageUrl;
    private int auctionId;
}
