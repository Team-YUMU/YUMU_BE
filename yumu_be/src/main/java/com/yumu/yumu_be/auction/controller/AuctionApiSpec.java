package com.yumu.yumu_be.auction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auction", description = "경매글")
public interface AuctionApiSpec {
    @Operation(summary = "경매글 등록", description = "경매글 등록 API")
    ResponseEntity<?> create();
    @Operation(summary = "경매글 수정", description = "경매글 수정 API")
    ResponseEntity<?> update();
    @Operation(summary = "경매글 조회", description = "경매글 조회 API")
    ResponseEntity<?> find();
//    @Operation(summary = "최근 경매글 조회", description = "경매글 최신순 조회 API")
//    ResponseEntity<?> findByLatest();
//    @Operation(summary = "인기 경매글 조회", description = "경매글 인기순 조회 API")
//    ResponseEntity<?> findByPopular();
    @Operation(summary = "경매글 삭제", description = "경매글 삭제 API")
    ResponseEntity<?> delete();
}
