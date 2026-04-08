package com.boli.biddingservice.service;

import com.boli.biddingservice.dto.BidRequest;
import com.boli.biddingservice.dto.BidResponse;
import com.boli.biddingservice.exception.InvalidBidException;
import com.boli.biddingservice.model.Bid;
import com.boli.biddingservice.repository.wrapper.BidRepository;
import com.boli.common.enums.AuctionStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidService {
    private final BidRepository bidRepository;
    private final AuctionCacheService auctionCacheService;

    @Transactional
    public BidResponse placeBid(BidRequest request, Long bidderId){
        log.info("bid_placement_initiated | auctionId={} | bidderId={}", request.getAuctionId(), bidderId);
        Map<Object, Object> auctionMetadata = auctionCacheService.getAuctionMetadata(request.getAuctionId());
        if(auctionMetadata.isEmpty() || !AuctionStatus.LIVE.name().equals(auctionMetadata.get("status"))){
            log.warn("bid_placement_failed | reason=auction_not_live | auctionId={}", request.getAuctionId());
            throw new InvalidBidException("Auction is not live or does not exist");
        }
        Double startingPrice = parseDoubleSafely(auctionMetadata.get("startingPrice"));
        Double minIncrement = parseDoubleSafely(auctionMetadata.get("minIncrement"));
        Double currentHighestBid = parseDoubleSafely(auctionMetadata.get("highestBid"));
        Double incomingAmount = request.getAmount();
        if(currentHighestBid == null){
            if(incomingAmount < startingPrice){
               log.warn("bid_placement_failed | reason=below_starting_price | auctionId={}", request.getAuctionId());
               throw new InvalidBidException("Bid must be at least the starting price of " + startingPrice);
            }
        }else{
            if(incomingAmount < (currentHighestBid + minIncrement)){
                log.warn("bid_placement_failed | reason=below_min_increment | auctionId={}", request.getAuctionId());
                throw new InvalidBidException("Bid must be at least of " + (currentHighestBid + minIncrement));
            }
        }
        auctionCacheService.updateHighestBid(request.getAuctionId(), bidderId, incomingAmount);
        Bid bid = Bid.builder().auctionId(request.getAuctionId()).bidderId(bidderId).amount(incomingAmount).build();
        bidRepository.save(bid);
        log.info("bid_placement_success | auctionId={} | bidId={} | amount={}",  bid.getAuctionId(), bid.getId(), bid.getAmount());
        return BidResponse.builder().id(bid.getId()).auctionId(bid.getAuctionId()).bidderId(bid.getBidderId()).amount(bid.getAmount()).isHighest(true).build();
    }

    private Double parseDoubleSafely(Object obj) {
        if (obj == null) {
            return null;
        }
        return Double.parseDouble(obj.toString());
    }
}