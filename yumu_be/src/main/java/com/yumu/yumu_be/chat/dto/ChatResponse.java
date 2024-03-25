package com.yumu.yumu_be.chat.dto;

import lombok.Getter;

@Getter
public class ChatResponse {

    private String memberId;
    private String message;

    public ChatResponse(String memberId, String message) {
        this.memberId = memberId;
        this.message = message;
    }

    public static ChatResponse of(String memberId, String message) {
        return new ChatResponse(memberId, message);
    }
    public static ChatResponse setJoinMessage(String memberId) {
        return new ChatResponse(memberId, memberId+"님이 경매에 참여하였습니다.");
    }
}
