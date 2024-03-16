package com.yumu.yumu_be.auction.service;

import com.yumu.yumu_be.art.repository.ArtRepository;
import com.yumu.yumu_be.art.repository.domain.Art;
import com.yumu.yumu_be.auction.controller.request.AuctionRequest;
import com.yumu.yumu_be.auction.controller.response.AuctionDto;
import com.yumu.yumu_be.auction.controller.response.AuctionResponse;
import com.yumu.yumu_be.auction.exception.AuctionNotFoundException;
import com.yumu.yumu_be.auction.repository.AuctionRepository;
import com.yumu.yumu_be.auction.repository.domain.Auction;
import com.yumu.yumu_be.image.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ArtRepository artRepository;
    private final S3Service s3Service;

    public AuctionService(AuctionRepository auctionRepository, ArtRepository artRepository, S3Service s3Service) {
        this.auctionRepository = auctionRepository;
        this.artRepository = artRepository;
        this.s3Service = s3Service;
    }
    @Transactional
    public void create(String memberId, AuctionRequest request, MultipartFile multipartFile) throws IOException {
        String imageUrl = s3Service.upload(multipartFile, memberId);
        Auction auction = Auction.of(request.getArtDescription(),request.getArtSize(),request.getArtCreatedDate(), request.getAuctionStartDate(), request.getAuctionEndDate(), request.getDefaultBid(), request.getNotice(), request.getReceiveType());
        artRepository.save(Art.of(request.getArtName(), imageUrl, memberId, auction));
        auctionRepository.save(auction);
    }

    public AuctionResponse find(int page, int size, String sort, String keyWord) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
//        검색
        if (!keyWord.isBlank()) {
            return searchMode(keyWord, pageRequest);
        }
        //인기
//        if (sort.equals("popular")) {
//            return artRepository.findByPopular(pageRequest);
//        }
        if (sort.equals("live")) {
            Page<Art> findLive = artRepository.findLive(pageRequest);
            return AuctionResponse.of(findLive.getNumber(),findLive.getTotalElements(),findLive.getTotalPages(),findLive.stream().map(AuctionDto::of).collect(Collectors.toList()) );
        }
        Page<Art> findLatest = artRepository.findLatest(pageRequest);
        return AuctionResponse.of(findLatest.getNumber(),findLatest.getTotalElements(),findLatest.getTotalPages(),findLatest.stream().map(AuctionDto::of).collect(Collectors.toList()) );
    }

    private AuctionResponse searchMode(String keyWord, PageRequest pageRequest) {
        Page<Art> findByKeyword = artRepository.findByKeyWord(keyWord, pageRequest);
        return AuctionResponse.of(findByKeyword.getNumber(),findByKeyword.getTotalElements(),findByKeyword.getTotalPages(),findByKeyword.stream()
                .map(AuctionDto::of)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void delete(int id) {
        Auction auction = auctionRepository.findById(id).orElseThrow(AuctionNotFoundException::new);
        auctionRepository.deleteById(id);
    }
}
