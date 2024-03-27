package com.yumu.yumu_be.auction.service;

import com.yumu.yumu_be.art.repository.ArtRepository;
import com.yumu.yumu_be.art.entity.Art;
import com.yumu.yumu_be.auction.dto.AuctionRequest;
import com.yumu.yumu_be.auction.dto.AuctionDetailDto;
import com.yumu.yumu_be.auction.dto.AuctionDto;
import com.yumu.yumu_be.auction.dto.AuctionResponse;
import com.yumu.yumu_be.auction.repository.AuctionRepository;
import com.yumu.yumu_be.auction.entity.Auction;
import com.yumu.yumu_be.exception.NotFoundException;
import com.yumu.yumu_be.image.S3Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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
    public void create(String memberId, AuctionRequest request) throws IOException {
        String imageUrl = s3Service.upload(request.getImage(), memberId);
        Auction auction = Auction.of(request.getArtDescription(),request.getArtSize(),request.getArtCreatedDate(), request.getAuctionStartDate(), request.getAuctionEndDate(), request.getDefaultBid(), request.getNotice(), request.getReceiveType());
        artRepository.save(Art.of(request.getArtName(),request.getArtSubTitle(), imageUrl, memberId, auction));
        auctionRepository.save(auction);
    }

    public AuctionResponse find(int page, int size, String sort, String keyWord) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
//        검색
        if (!keyWord.isBlank()) {
            if (sort.equals("popular")) {
                return getSearchAndPopular(keyWord, pageRequest, sort);
            }
            return getSearch(keyWord, pageRequest);
        }
        //인기
        if (sort.equals("popular")) {
            Page<Art> findPopular = artRepository.findByPopular(pageRequest);
            return AuctionResponse.of(findPopular.getNumber(), findPopular.getTotalElements(), findPopular.getTotalPages(), findPopular.stream().map(AuctionDto::of).collect(Collectors.toList()));
        }
        if (sort.equals("live")) {
            Page<Art> findLive = artRepository.findLive(pageRequest);
            return AuctionResponse.of(findLive.getNumber(),findLive.getTotalElements(),findLive.getTotalPages(),findLive.stream().map(AuctionDto::of).collect(Collectors.toList()) );
        }
        if (sort.equals("liveSoon")) {
            System.out.println(LocalDateTime.now());
            pageRequest = PageRequest.of(page, size, Sort.by("auction_start_date").ascending());
            Page<Auction> findLiveSoon = auctionRepository.findLiveSoon(pageRequest,LocalDateTime.now() );
            System.out.println("임박 라이브 조회 완료 로그"+findLiveSoon.get().findFirst().get().getArt().getArtName());
            return AuctionResponse.of(findLiveSoon.getNumber(),findLiveSoon.getTotalElements(),findLiveSoon.getTotalPages(),findLiveSoon.stream().map(auction-> AuctionDto.of(auction.getArt())).collect(Collectors.toList()) );
        }
        Page<Art> findLatest = artRepository.findLatest(pageRequest);
        return AuctionResponse.of(findLatest.getNumber(),findLatest.getTotalElements(),findLatest.getTotalPages(),findLatest.stream().map(AuctionDto::of).collect(Collectors.toList()) );
    }

    private AuctionResponse getSearch(String keyWord, PageRequest pageRequest) {
        Page<Art> findByKeyword = artRepository.findByKeyWord(keyWord, pageRequest);
        return AuctionResponse.of(findByKeyword.getNumber(),findByKeyword.getTotalElements(),findByKeyword.getTotalPages(),findByKeyword.stream()
                .map(AuctionDto::of)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void delete(int id) {
        Auction auction = auctionRepository.findById(id).orElseThrow(NotFoundException.NotFoundAuctionException::new);
        String imageUrl = auction.getArt().getArtImage();
        auctionRepository.deleteById(id);
        s3Service.deleteFile(imageUrl);
    }

    @Transactional
    public void update(int id, String memberId, AuctionRequest request) throws IOException {
        Auction auction = auctionRepository.findById(id).orElseThrow(NotFoundException.NotFoundAuctionException::new);
        String ordImageUrl = auction.getArt().getArtImage();
        String newImageUrl = s3Service.updateFile(request.getImage(), ordImageUrl, memberId);
        auction.updateTo(request.getArtDescription(),request.getArtSize(),request.getArtCreatedDate(), request.getAuctionStartDate(), request.getAuctionEndDate(), request.getDefaultBid(), request.getNotice(), request.getReceiveType());
        auction.getArt().updateTo(request.getArtName(), request.getArtSubTitle(), newImageUrl, memberId, auction);
    }

    public AuctionDetailDto getDetail(int id) {
        Auction auction = auctionRepository.findById(id).orElseThrow(NotFoundException.NotFoundAuctionException::new);
        return AuctionDetailDto.of(auction);
    }

    private AuctionResponse getSearchAndPopular(String keyWord, PageRequest pageRequest, String sort) {
        Page<Art> findByKeyword = artRepository.findByKeyWordSortByWishCnt(keyWord, pageRequest, sort);
        return AuctionResponse.of(findByKeyword.getNumber(),findByKeyword.getTotalElements(),findByKeyword.getTotalPages(),findByKeyword.stream()
                .map(AuctionDto::of)
                .collect(Collectors.toList()));
    }
}
