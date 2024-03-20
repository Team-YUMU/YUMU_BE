package com.yumu.yumu_be.auction.repository.domain;

import com.yumu.yumu_be.auction.exception.NoSuchReceiveTypeException;

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
        return Arrays.stream(Receive.values())
                .filter(t -> t.getType().equals(type))
                .findFirst()
                .orElseThrow(NoSuchReceiveTypeException::new);
    }
}
