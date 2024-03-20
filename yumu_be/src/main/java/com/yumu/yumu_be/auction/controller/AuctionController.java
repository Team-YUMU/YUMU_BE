package com.yumu.yumu_be.auction.controller;

import com.yumu.yumu_be.auction.controller.request.AuctionRequest;
import com.yumu.yumu_be.auction.controller.response.AuctionResponse;
import com.yumu.yumu_be.auction.service.AuctionService;
import com.yumu.yumu_be.auction.controller.response.CommonResponse;
import com.yumu.yumu_be.wishList.service.WishListService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping
    public ResponseEntity<CommonResponse> create(@AuthenticationPrincipal UserDetails user, @RequestPart("request") AuctionRequest request, @RequestPart("image") MultipartFile multipartFile) {
        try {
            auctionService.create(user.getUsername(), request, multipartFile);
            return ResponseEntity.ok(CommonResponse.of(true, null));
        } catch (Exception e) {
            System.out.println("error message = "+e.getMessage());
            return ResponseEntity.ok(CommonResponse.of(false, e.getMessage()));
        }
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse> update(@AuthenticationPrincipal UserDetails user,@PathVariable int id, @RequestPart("request") AuctionRequest request, @RequestPart("image") MultipartFile multipartFile) {
        try {
            auctionService.update(id, user.getUsername(), request, multipartFile);
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

    @Override
    @PostMapping("/wish/{id}")
    public ResponseEntity<String> wishList(@AuthenticationPrincipal UserDetails user, @PathVariable int id) {
        return ResponseEntity.ok(wishListService.addWishList(user.getUsername(), id));
    }
}
