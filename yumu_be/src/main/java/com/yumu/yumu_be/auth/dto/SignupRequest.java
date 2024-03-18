package com.yumu.yumu_be.auth.dto;

import com.yumu.yumu_be.valid.annotation.PasswordCheck;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequest {

    //닉네임, 이메일, 비밀번호
    //비밀번호 - 6~16자, 영문 대소문자, 숫자, 특수문자 중 2개 이상 사용
    //닉네임 - 2-10자, 한글, 영문, 숫자 가능 특수문자 불가
    //이메일 형식
    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "[a-zA-Z0-9가-힣]{2,10}", message = "닉네임은 영문 대소문자, 숫자, 한글로 구성한 2-10자 사이로 입력해주세요.")
    private String nickname;

    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @PasswordCheck(message = "비밀번호 형식이 맞지 않습니다.")
    private String password;

    @NotBlank
    private String checkPassword;
}
