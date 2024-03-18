package com.yumu.yumu_be.auction.exception;

public class AuctionNotFoundException extends RuntimeException{
    private static final String AUCTION_NOT_FOUND_MESSAGE = "[ERROR] 경매글을 찾을 수 없습니다.";

    public AuctionNotFoundException() {
        super(AUCTION_NOT_FOUND_MESSAGE);
    }
}
