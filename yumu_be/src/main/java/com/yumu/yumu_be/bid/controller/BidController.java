package com.yumu.yumu_be.bid.controller;

import com.yumu.yumu_be.bid.dto.HighestPriceResponse;
import com.yumu.yumu_be.bid.service.BidService;
import com.yumu.yumu_be.common.dto.CommonResponse;
import com.yumu.yumu_be.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BidController {

    private final BidService bidService;

    //입찰 - 이거 post 맞나,.? 실질적으로 저장은 안 함, redis에만 함
    @GetMapping("/bid")
    public ResponseEntity<CommonResponse> bid(@RequestParam int auctionId, @RequestBody Double price, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(bidService.bid(auctionId, price, userDetails.getMember().getId()));
    }

    //낙찰 - 이건 action 데이터 수정 + 구매 리스트 추가인데 post? put?
    @PostMapping("/bid")
    public ResponseEntity<CommonResponse> successBid(@RequestParam int auctionId) {
        return ResponseEntity.ok(bidService.successBid(auctionId));
    }

    //최초 입장 최고가
    @GetMapping("/highest-bid")
    public ResponseEntity<HighestPriceResponse> firstHighestPrice(@RequestParam int auctionId) {
        return ResponseEntity.ok(bidService.firstHighestPrice(auctionId));
    }

}
