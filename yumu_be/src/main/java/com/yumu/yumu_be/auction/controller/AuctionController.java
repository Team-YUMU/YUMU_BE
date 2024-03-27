package com.yumu.yumu_be.auction.controller;

import com.yumu.yumu_be.auction.dto.AuctionRequest;
import com.yumu.yumu_be.auction.dto.AuctionDetailDto;
import com.yumu.yumu_be.auction.dto.AuctionResponse;
import com.yumu.yumu_be.auction.service.AuctionService;
import com.yumu.yumu_be.wishList.service.WishListService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auction")
public class AuctionController implements AuctionApiSpec{

    private final AuctionService auctionService;
    private final WishListService wishListService;

    public AuctionController(AuctionService auctionService, WishListService wishListService) {
        this.auctionService = auctionService;
        this.wishListService = wishListService;
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> create(@AuthenticationPrincipal UserDetails user, @ModelAttribute AuctionRequest request) throws IOException {
        System.out.println("경매등록 시작합니다");
        auctionService.create(user.getUsername(), request);
        return ResponseEntity.ok("경매글이 성공적으로 등록되었습니다.");
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> update(@AuthenticationPrincipal UserDetails user,@PathVariable int id, @ModelAttribute AuctionRequest request) throws IOException {
        auctionService.update(id, user.getUsername(), request);
        return ResponseEntity.ok("경매글이 성공적으로 수정되었습니다.");

    }

    @Override
    @GetMapping(value = "/paging")
    public ResponseEntity<AuctionResponse> find(@RequestParam(value = "page", required = false) int page,
                                                @RequestParam(value = "size", required = false) int size,
                                                @RequestParam(value = "sort", required = false) String sort,
                                                @RequestParam(value = "keyword", required = false) String keyWord) {
        return ResponseEntity.ok(auctionService.find(page, size, sort, keyWord));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        auctionService.delete(id);
        return ResponseEntity.ok("성공적으로 삭제되었습니다.");

    }

    @Override
    @PostMapping("/wish/{id}")
    public ResponseEntity<String> wishList(@AuthenticationPrincipal UserDetails user, @PathVariable int id) {
        return ResponseEntity.ok(wishListService.addWishList(user.getUsername(), id));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<AuctionDetailDto> getDetail(@PathVariable int id) {
        return ResponseEntity.ok(auctionService.getDetail(id));
    }
}
