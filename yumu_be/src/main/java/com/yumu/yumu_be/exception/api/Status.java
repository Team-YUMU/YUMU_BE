package com.yumu.yumu_be.exception.api;

import lombok.Getter;

@Getter
public enum Status {

    //400 Bad Request
    BAD_REQUEST(400, "Bad Request"),
    DUPLICATED_EMAIL(400, "이미 존재하는 이메일입니다."),
    DUPLICATED_NICKNAME(400, "이미 존재하는 닉네임입니다."),
    DUPLICATED_MEMBER(400, "이미 가입 되어있는 회원입니다."),
    INVALID_EMAIL(400, "올바르지 않는 이메일 형식입니다."),
    INVALID_PASSWORD(400, "올바르지 않는 비밀번호 형식입니다."),
    INVALID_NICKNAME(400, "올바르지 않는 닉네임 형식입니다."),


    //404 Not Found
    NOT_FOUND(404, "Not Found"),
    NOT_FOUND_MEMBER(404, "회원을 찾을 수 없습니다."),
    NOT_FOUND_ACCESS_TOKEN(404, "어세스 토큰이 존재 하지않습니다."),
    NOT_FOUND_REFRESH_TOKEN(404, "리프레시 토큰이 존재 하지않습니다."),
    NOT_FOUND_AUCTION(404, "경매글이 존재 하지않습니다."),
    NOT_FOUND_RECEIVE_TYPE(404, "수령방법이 존재 하지않습니다."),


    //403 Forbidden
    FORBIDDEN(403, "Forbidden"),
    INVALID_MEMBER(403, "아이디 비밀번호를 확인 해주세요."),
    INVALID_ACCESS_TOKEN(403, "잘못된 어세스 토큰 입니다."),
    INVALID_REFRESH_TOKEN(403, "잘못된 리프레시 토큰 입니다."),
    NON_AUTHORITY_MEMBER(403, "접근 권한이 없습니다.");


    Status(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    private final int code;
    private final String msg;
}
