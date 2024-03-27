package com.yumu.yumu_be.member.dto;

import com.yumu.yumu_be.member.entity.LoginStatus;
import com.yumu.yumu_be.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    private String nickname;
    private String email;
    private String introduce;
    private String profileImage;
    private LoginStatus loginStatus;

    public ProfileResponse(Member member) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.introduce = member.getIntroduce();
        this.profileImage = member.getProfileImage();
        this.loginStatus = member.getNowLogin();
    }
}
