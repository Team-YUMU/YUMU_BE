package com.yumu.yumu_be.bid.service;

import com.yumu.yumu_be.art.entity.Art;
import com.yumu.yumu_be.art.entity.Status;
import com.yumu.yumu_be.auction.entity.Auction;
import com.yumu.yumu_be.auction.repository.AuctionRepository;
import com.yumu.yumu_be.bid.dto.HighestPriceResponse;
import com.yumu.yumu_be.common.dto.CommonResponse;
import com.yumu.yumu_be.exception.BadRequestException;
import com.yumu.yumu_be.exception.NotFoundException;
import com.yumu.yumu_be.lock.DistributeLock;
import com.yumu.yumu_be.member.entity.Member;
import com.yumu.yumu_be.member.entity.PurchaseHistory;
import com.yumu.yumu_be.member.entity.SaleHistory;
import com.yumu.yumu_be.member.repository.MemberRepository;
import com.yumu.yumu_be.member.repository.PurchaseHistoryRepository;
import com.yumu.yumu_be.member.repository.SaleHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final String PREFIX = "AUCTION:";
    private final RedisBidService redisBidService;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final SaleHistoryRepository saleHistoryRepository;
    private final SimpMessageSendingOperations messageTemplate;

    @Override
    @DistributeLock(key = "#auctionId")
    @Transactional
    public CommonResponse bid(int auctionId, Double price, Long memberId) {
        String key = serializeKey(auctionId); //redis 키값 생성 - AUCTION:{auctionId} 형태
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(NotFoundException.NotFoundAuctionException::new);   //존재하는 auction인지 확인
        Member member = memberRepository.findById(memberId).orElseThrow(NotFoundException.NotFoundMemberException::new);
        String bidder = member.getNickname();
        LocalDateTime expire = auction.getAuctionEndDate();     //키 삭제 기간을 경매 끝나는 시간으로 설정

        //현재 입찰가 존재 여부 확인 - 존재하지 않을 경우, 바로 저장
        if (redisBidService.isExistBid(key)) {     //입찰이 존재할 경우, 가격 비교
            Double recentPrice = redisBidService.getBidPrice(key, memberId);
            if (Double.compare(price, recentPrice) <= 0) {              //입찰가보다 낮은 가격일 경우, exception
                throw new BadRequestException.LowBidPriceException();
            }
            redisBidService.deleteBid(key);     //입찰가보다 높을 경우, 이전 입찰가 삭제 후 저장
        }

        redisBidService.bid(key, memberId, price, expire);

        //웹소켓으로 최고가 정보 보내기
        HighestPriceResponse response = new HighestPriceResponse(auctionId, bidder, price.longValue());
        highestPriceMessage(response);

        return new CommonResponse("입찰 완료");
    }

    @Override
    public CommonResponse successBid(int auctionId) {
        //art의 status와 auction의 낙찰자, 낙찰가 수정
        String key = serializeKey(auctionId);
        Long successBidderId = redisBidService.getBidder(key);   //낙찰자의 id값
        Double price = Math.ceil(redisBidService.getBidPrice(key, successBidderId) / 100) * 100;    //낙찰가 - 100단위에서 올림처리
        Long successPrice = price.longValue();

        Auction auction = auctionRepository.findById(auctionId).orElseThrow(NotFoundException.NotFoundAuctionException::new);
        Art art = auction.getArt();
        Member successBidder = memberRepository.findById(successBidderId).orElseThrow(NotFoundException.NotFoundMemberException::new);
        auction.updateSuccessBid(successPrice, successBidder.getNickname());
        art.updateStatus(Status.DONE);

        //구매자의 구매목록 추가, 판매자의 판매목록 상태 수정
        PurchaseHistory purchaseHistory = new PurchaseHistory(art, successPrice);
        purchaseHistoryRepository.save(purchaseHistory);

        SaleHistory saleHistory = saleHistoryRepository.findByAuctionId(auctionId);
        saleHistory.updateStatus(Status.DONE);

        return new CommonResponse("낙찰 완료");
    }

    @Override
    public HighestPriceResponse firstHighestPrice(int auctionId) {
        String key = serializeKey(auctionId);
        if (!redisBidService.isExistBid(key)) {
            return null;
        }
        Long bidderId = redisBidService.getBidder(key);
        Long price = redisBidService.getBidPrice(key, bidderId).longValue();
        Member member = memberRepository.findById(bidderId).orElseThrow(NotFoundException.NotFoundMemberException::new);
        return new HighestPriceResponse(auctionId, member.getNickname(), price);
    }

    public void highestPriceMessage(HighestPriceResponse response) {
        messageTemplate.convertAndSend("/bid/sub/" + response.getAuctionId(), response);
    }

    private String serializeKey(int auctionId) {
        return PREFIX + auctionId;
    }
}
