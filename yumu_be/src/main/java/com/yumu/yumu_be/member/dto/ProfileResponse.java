package com.yumu.yumu_be.member.dto;

import com.yumu.yumu_be.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProfileResponse {
    private String nickname;
    private String email;
    private String introduce;
    private String snsLink;
    private String profileImage;
    private String provider;

    public ProfileResponse(Member member) {
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.introduce = member.getIntroduce();
        this.snsLink = member.getSnsLink();
        this.profileImage = member.getProfileImage();
        this.provider = member.getProvider();
    }
}
