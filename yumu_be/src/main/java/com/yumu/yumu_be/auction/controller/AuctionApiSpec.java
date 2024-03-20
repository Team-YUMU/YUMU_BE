package com.yumu.yumu_be.auction.controller;

import com.yumu.yumu_be.auction.controller.request.AuctionRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.userdetails.User;

@Tag(name = "Auction", description = "경매글")
public interface AuctionApiSpec {
    @Operation(summary = "경매글 등록", description = "경매글 등록 API")
    ResponseEntity<?> create(UserDetails user, AuctionRequest request, MultipartFile multipartFile);
    @Operation(summary = "경매글 수정", description = "경매글 수정 API")
    ResponseEntity<?> update(UserDetails user, int id,AuctionRequest request, MultipartFile multipartFile);
    @Operation(summary = "경매글 조회", description = "경매글 조회 API")
    ResponseEntity<?> find(int page, int size, String sort, String keyWord);
//    @Operation(summary = "최근 경매글 조회", description = "경매글 최신순 조회 API")
//    ResponseEntity<?> findByLatest();
//    @Operation(summary = "인기 경매글 조회", description = "경매글 인기순 조회 API")
//    ResponseEntity<?> findByPopular();
    @Operation(summary = "경매글 삭제", description = "경매글 삭제 API")
    ResponseEntity<?> delete(int id);
    @Operation(summary = "경매글 짬 기능", description = "경매글 짬 기능 API")
    ResponseEntity<?> wishList(UserDetails user, int id);
}
