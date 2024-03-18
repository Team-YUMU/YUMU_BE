package com.yumu.yumu_be.auth.dto;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String nickname;
    private String password;
}
