package com.yumu.yumu_be.member.entity;

import com.yumu.yumu_be.exception.BadRequestException;
import com.yumu.yumu_be.member.dto.ProfileRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String nickname;

    @NotNull
    private String password;

    private String introduce;

    private String snsLink;

    private String profileImage;
    
    //카카오
    private String provider;
    private Long providerId;

    public Member(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public Member(String nickname, String email, String password, String provider, Long providerId) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updateProfile(ProfileRequest request, String imageUrl) {
        if (!Pattern.matches("[a-zA-Z0-9가-힣]{2,10}", request.getNickname())) {
            throw new BadRequestException.InvalidNicknameException();
        } else {
            this.nickname = request.getNickname();
        }

        if (!request.getIntroduce().isEmpty()) {
            this.introduce = request.getIntroduce();
        }

        if (!request.getSnsLink().isEmpty()) {
            this.snsLink = request.getSnsLink();
        }

        if (!imageUrl.isEmpty()) {
            this.profileImage = imageUrl;
        }
    }

    public void updateProvider(Long providerId, String provider) {
        this.provider = provider;
        this.providerId = providerId;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
