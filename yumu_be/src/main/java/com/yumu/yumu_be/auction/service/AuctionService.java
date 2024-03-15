package com.yumu.yumu_be.auction.service;

import com.yumu.yumu_be.art.repository.ArtRepository;
import com.yumu.yumu_be.art.repository.domain.Art;
import com.yumu.yumu_be.auction.controller.request.AuctionRequest;
import com.yumu.yumu_be.auction.repository.AuctionRepository;
import com.yumu.yumu_be.auction.repository.domain.Auction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ArtRepository artRepository;

    public AuctionService(AuctionRepository auctionRepository, ArtRepository artRepository) {
        this.auctionRepository = auctionRepository;
        this.artRepository = artRepository;
    }
    @Transactional
    public void create(String memberId, AuctionRequest request) {
        Auction auction = Auction.of(request.getArtDescription(),request.getArtSize(),request.getArtCreatedDate(), request.getAuctionStartDate(), request.getAuctionEndDate(), request.getDefaultBid(), request.getNotice(), request.getReceiveType());
        artRepository.save(Art.of(request.getArtName(), request.getArtImage(), memberId,auction));
        auctionRepository.save(auction);
    }
}
