package com.yumu.yumu_be.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final StringRedisTemplate stringRedisTemplate;

    //redis에 refresh token 저장 (email-refreshToken 형태)
    public void addRefreshTokenByRedis(String email, String refreshToken, Duration duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(email, refreshToken, duration);
    }

    //redis에서 refresh token 값 가져오기
    public String getRefreshTokenByRedis(String email) {
        return stringRedisTemplate.opsForValue().get(email);
    }

    //redis의 refresh token 값 지우기
    public void deleteRefreshTokenByRedis(String email) {
        stringRedisTemplate.delete(email);
    }

    //redis에 access token의 남은 유효기간 동안 로그아웃 기록 저장
    public void logoutAccessTokenByRedis(String accessToken, String logout, Long expireTime, TimeUnit milliseconds) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(accessToken, logout, expireTime, milliseconds);
    }

}
