package com.yumu.yumu_be.auction.controller;

import com.yumu.yumu_be.auction.controller.request.AuctionRequest;
import com.yumu.yumu_be.auction.controller.response.AuctionResponse;
import com.yumu.yumu_be.auction.service.AuctionService;
import com.yumu.yumu_be.auction.controller.response.CommonResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auction")
public class AuctionController implements AuctionApiSpec{

    private final AuctionService auctionService;

    public AuctionController(AuctionService auctionService) {
        this.auctionService = auctionService;
    }

    @Override
    @PostMapping
    public ResponseEntity<CommonResponse> create(@AuthenticationPrincipal UserDetails user, @RequestPart("request") AuctionRequest request, @RequestPart("image") MultipartFile multipartFile) {
        System.out.println("멤버 id = "+user.getUsername());
        try {
            System.out.println("생성 시작");
            auctionService.create(user.getUsername(), request, multipartFile);
            System.out.println("최종 pass");
            return ResponseEntity.ok(CommonResponse.of(true, null));
        } catch (Exception e) {
            return ResponseEntity.ok(CommonResponse.of(false, e.getMessage()));
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> update(@AuthenticationPrincipal UserDetails user,@PathVariable int id, @RequestPart("request") AuctionRequest request, @RequestPart("image") MultipartFile multipartFile) {
        try {
            auctionService.update(id,"testUser", request, multipartFile);
            return ResponseEntity.ok(CommonResponse.of(true, null));
        } catch (Exception e) {
            return ResponseEntity.ok(CommonResponse.of(false, e.getMessage()));
        }
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
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse> delete(@PathVariable int id) {
        try {
            auctionService.delete(id);
            return ResponseEntity.ok(CommonResponse.of(true, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
