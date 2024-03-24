package com.yumu.yumu_be.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ProfileRequest {
    private String nickname;
    private String introduce;
    private String snsLink;
    private MultipartFile profileImage;
}
