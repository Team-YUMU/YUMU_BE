package com.yumu.yumu_be.auction.controller;

import com.yumu.yumu_be.auction.controller.request.AuctionRequest;
import com.yumu.yumu_be.auction.controller.response.AuctionResponse;
import com.yumu.yumu_be.auction.service.AuctionService;
import com.yumu.yumu_be.auction.controller.response.CommonResponse;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auction")
public class AuctionController implements AuctionApiSpec{

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Override
    @PostMapping
    public ResponseEntity<CommonResponse> create(String memberId, @RequestBody AuctionRequest request) {
        try {
            auctionService.create("testUser", request);
            return ResponseEntity.ok(CommonResponse.of(true, null));
        } catch (Exception e) {
            return ResponseEntity.ok(CommonResponse.of(false, e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> update() {
        return null;
    }

    @Override
    @GetMapping(value = "/paging")
    public ResponseEntity<AuctionResponse> find(@RequestParam(value = "page", required = false) int page,
                                                @RequestParam(value = "size", required = false) int size,
                                                @RequestParam(value = "sort", required = false) String sort,
                                                @RequestParam(value = "keyword", required = false) String keyWord) {
        try {
            return ResponseEntity.ok(auctionService.find(page, size, sort, keyWord));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<?> delete() {
        return null;
    }
}
