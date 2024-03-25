package com.yumu.yumu_be.chat.dto;

import lombok.Getter;

@Getter
public class ChatResponse {

    private String memberId;
    private String message;
    private String type;

    public ChatResponse(String memberId, String message, String type) {
        this.memberId = memberId;
        this.message = message;
        this.type = type;
    }

    public static ChatResponse setChat(String memberId, String message) {
        return new ChatResponse(memberId, message, "CHAT");
    }
    public static ChatResponse setJoinMessage(String memberId) {
        return new ChatResponse(memberId, memberId+"님이 경매에 참여하였습니다.","JOIN");
    }
}