package com.yumu.yumu_be.bid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class HighestBidController {

    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/{auctionId}")
    public void message(@DestinationVariable("auctionId") int auctionId) {
        messagingTemplate.convertAndSend("/bid/sub/" + auctionId, "최고가 소켓 연결 완료");
    }
}
