package com.yumu.yumu_be.member.dto;

import lombok.Getter;

@Getter
public class PasswordRequest {
    private String newPassword;
    private String newCheckPassword;
}
