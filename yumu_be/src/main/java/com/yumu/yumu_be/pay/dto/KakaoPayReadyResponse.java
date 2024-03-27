package com.yumu.yumu_be.pay.dto;

import lombok.Getter;

@Getter
public class KakaoPayReadyResponse {
    private String tid; //결제 고유번호
    private String mobileRedirectUrl;
    private String webRedirectUrl;
    private String createdAt;
}
