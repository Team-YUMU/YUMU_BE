package com.yumu.yumu_be.stomp;

import com.yumu.yumu_be.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("websocket 연결 message:" + message);
        System.out.println("websocket 연결 헤더 : " + message.getHeaders());
        System.out.println("websocket 연결 토큰" + accessor.getNativeHeader("Authorization"));

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            jwtUtil.validationToken(Objects.requireNonNull(accessor.getFirstNativeHeader("Authorization")));
        }

        return message;
    }
}
