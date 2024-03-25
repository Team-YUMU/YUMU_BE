package com.yumu.yumu_be.bid.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RedisBidService {
    private final RedisTemplate<String, Long> bidRequestTemplate;

    public Boolean isExistBid(String key) {return bidRequestTemplate.hasKey(key); }

    public void bid(String key, Long memberId, Double price, LocalDateTime expire) {
        bidRequestTemplate.opsForZSet().add(key, memberId, price);
        bidRequestTemplate.expireAt(key, Instant.from(expire));     //만료날짜 세팅
    }

    public Double getBidPrice(String key, Long memberId) {
        return bidRequestTemplate.opsForZSet().score(key, memberId);
    }

    public Long getBidder(String key) {
        return bidRequestTemplate.opsForValue().get(key);
    }

    public void deleteBid(String key) {
        bidRequestTemplate.delete(key);
    }

}
