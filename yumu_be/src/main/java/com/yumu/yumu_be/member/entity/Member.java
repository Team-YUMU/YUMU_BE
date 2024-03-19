package com.yumu.yumu_be.member.entity;

import com.yumu.yumu_be.member.dto.ProfileRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String introduce = "";

    private String snsLink = "";

    private String address = "";

    private String profileImage = "images/default.jpg";

    public Member(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public void updateProfile(ProfileRequest request, String imageUrl) {
        this.nickname = request.getNickname();
        this.introduce = request.getIntroduce();
        this.snsLink = request.getSnsLink();
        this.profileImage = imageUrl;
    }
}
