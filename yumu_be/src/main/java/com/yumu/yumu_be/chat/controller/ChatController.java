package com.yumu.yumu_be.chat.controller;

import com.yumu.yumu_be.chat.dto.ChatRequest;
import com.yumu.yumu_be.chat.dto.ChatResponse;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @MessageMapping("/{liveId}/chat.sendMessage")
    @SendTo("/liveRoom/{liveId}")
    public ChatResponse sendMessage(@DestinationVariable int liveId, ChatRequest request) {
        System.out.println("send message : "+request.getMessage());
        System.out.println("send message : "+request.getMessage());
        return ChatResponse.setChat(request.getMemberId(), request.getMessage());
    }

    @MessageMapping("/{liveId}/chat.addUser")
    @SendTo("/liveRoom/{liveId}")
    public ChatResponse addUser(@DestinationVariable int liveId, @AuthenticationPrincipal UserDetails user) {
        return ChatResponse.setJoinMessage(user.getUsername());
    }
}


