package com.yumu.yumu_be.common;

public enum Receive {
    EMAIL("이메일"),
    MAIL("우편"),
    DELIVERY("배송"),
    DIRECT("직접거래");

    private String type;

    Receive(String type) {
        this.type = type;
    }
}
