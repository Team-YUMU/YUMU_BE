package com.yumu.yumu_be.auction.entity;

import com.yumu.yumu_be.exception.NotFoundException;

import java.util.Arrays;

public enum Receive {
    EMAIL("이메일"),
    MAIL("우편"),
    DELIVERY("배송"),
    DIRECT("직접거래"),
    ETC("기타");

    private String type;

    Receive(String type) {
        this.type = type;
    }

    private String getType() {
        return type;
    }

    public static Receive of(String type) {
        System.out.println("recieve type : "+type);
        return Arrays.stream(Receive.values())
                .filter(t -> t.getType().equals(type))
                .findFirst()
                .orElseThrow(NotFoundException.NotFoundReceiveTypeException::new);
    }
}
