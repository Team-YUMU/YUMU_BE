package com.yumu.yumu_be.member.entity;

public enum RandomImage {
    IMAGE1("https://yumu-image.s3.ap-northeast-2.amazonaws.com/default/프로필시안_1_1.svg"),
    IMAGE2("https://yumu-image.s3.ap-northeast-2.amazonaws.com/default/프로필시안_1_2.svg"),
    IMAGE3("https://yumu-image.s3.ap-northeast-2.amazonaws.com/default/프로필시안_1_3.svg"),
    IMAGE4("https://yumu-image.s3.ap-northeast-2.amazonaws.com/default/프로필시안_1_4.svg");

    private final String type;

    RandomImage(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
