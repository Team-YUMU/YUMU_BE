package com.yumu.yumu_be.pay.service;

import com.yumu.yumu_be.auction.entity.Auction;
import com.yumu.yumu_be.auction.repository.AuctionRepository;
import com.yumu.yumu_be.exception.BadRequestException;
import com.yumu.yumu_be.exception.NotFoundException;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.pay.dto.KakaoPayApproveResponse;
import com.yumu.yumu_be.pay.dto.KakaoPayReadyResponse;
import com.yumu.yumu_be.pay.dto.KakaoPayRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class KakaoPayService {

    @Value("${kakaopay.cid}")
    private String cid;

    @Value("${kakaopay.adminkey}")
    private String adminKey;

    private final AuctionRepository auctionRepository;
    private final StringRedisTemplate stringRedisTemplate;

    //결제 요청
    public KakaoPayReadyResponse kakaoPayReady(KakaoPayRequest kakaoPayRequest, Member member, HttpServletRequest request) {
        //카카오페이 결제 요청 양식 세팅
        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        String partnerUserId = member.getNickname();

        Auction auction = auctionRepository.findById(kakaoPayRequest.getAuctionId()).orElseThrow(NotFoundException.NotFoundAuctionException::new);
        String price = Long.toString(auction.getWinningBid());

        parameter.add("cid", cid);
        parameter.add("partner_order_id", "yumu");
        parameter.add("partner_user_id", partnerUserId);
        parameter.add("item_name", kakaoPayRequest.getArtTitle());
        parameter.add("quantity", "1");
        parameter.add("total_amount", price);
        parameter.add("tax_free_amount", "0");
        parameter.add("approval_url", "http://43.200.219.117:8080/api/v1/kakaopay/success");
        parameter.add("cancel_url", "http://43.200.219.117:8080/api/v1/kakaopay/cancel");
        parameter.add("fail_url", "http://43.200.219.117:8080/api/v1/kakaopay/fail");

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameter, this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();
        KakaoPayReadyResponse readyResponse =  restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/ready",
                requestEntity,
                KakaoPayReadyResponse.class
        );

        HttpSession session = request.getSession();
        session.setAttribute("tid", readyResponse.getTid());
        session.setAttribute("partnerUserId", partnerUserId);

        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(readyResponse.getTid(), partnerUserId);

        return readyResponse;
    }

    //결제 완료 승인
    public KakaoPayApproveResponse kakaoPayApprove(String pgToken, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String tid = session.getAttribute("tid").toString();
        String partnerUserId = session.getAttribute("partnerUserId").toString();

        if (!partnerUserId.equals(stringRedisTemplate.opsForValue().get(tid))) {
            throw new BadRequestException.NotMatchTidException();
        }

        // 카카오 승인 형식 세팅
        MultiValueMap<String, String> parameter = new LinkedMultiValueMap<>();
        parameter.add("cid", cid);
        parameter.add("tid", tid);
        parameter.add("partner_order_id", "yumu");
        parameter.add("partner_user_id", partnerUserId);
        parameter.add("pg_token", pgToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameter, this.getHeaders());

        RestTemplate restTemplate = new RestTemplate();

        KakaoPayApproveResponse approveResponse = restTemplate.postForObject(
                "https://kapi.kakao.com/v1/payment/approve",
                requestEntity,
                KakaoPayApproveResponse.class);

        stringRedisTemplate.delete(tid);

        return approveResponse;
    }

    //카카오가 요구하는 헤더값 세팅
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        String auth = "KakaoAk " + adminKey;
        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return httpHeaders;
    }
}
