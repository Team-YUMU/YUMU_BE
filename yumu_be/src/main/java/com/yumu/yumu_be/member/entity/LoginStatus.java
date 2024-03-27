package com.yumu.yumu_be.member.entity;

public enum LoginStatus {
    DEFAULT("일반 로그인"),
    KAKAO("카카오 로그인");

    private final String type;

    LoginStatus(String type) {
        this.type = type;
    }
}
