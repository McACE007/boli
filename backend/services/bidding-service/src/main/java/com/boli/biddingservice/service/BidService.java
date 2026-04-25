package com.boli.biddingservice.service;

import com.boli.biddingservice.dto.BidRequest;
import com.boli.biddingservice.dto.BidResponse;
import com.boli.biddingservice.exception.InvalidBidException;
import com.boli.biddingservice.kafka.BidEvent;
import com.boli.biddingservice.kafka.BidEventPayload;
import com.boli.biddingservice.kafka.BidEventProducer;
import com.boli.biddingservice.model.Bid;
import com.boli.biddingservice.repository.wrapper.BidRepository;
import com.boli.common.enums.BidEventType;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidService {
    private final BidRepository bidRepository;
    private final AuctionCacheService auctionCacheService;
    private final BidEventProducer bidEventProducer;


    @Transactional
    public BidResponse placeBid(BidRequest request, Long bidderId){
        log.info("bid_placement_initiated | auctionId={} | bidderId={}", request.getAuctionId(), bidderId);

        // 1. Let Redis handle the atomic validation and update
        String result = auctionCacheService.attemptBidAtomically(request.getAuctionId(), bidderId, request.getAmount());

        // 2. Handle the result
        switch (result) {
            case "NOT_LIVE" -> throw new InvalidBidException("Auction is not live or does not exist");
            case "LOW_STARTING_PRICE" -> throw new InvalidBidException("Bid must be at least the starting price");
            case "LOW_BID" -> throw new InvalidBidException("Bid is not high enough to beat the current highest bid + increment");
            case "SUCCESS" -> log.debug("atomic_bid_accepted | auctionId={}", request.getAuctionId());
            default -> throw new IllegalStateException("Unexpected Redis script result: " + result);
        }

        Bid bid = Bid.builder().auctionId(request.getAuctionId()).bidderId(bidderId).amount(request.getAmount()).build();
        bidRepository.save(bid);

        // 3. Publish to Kafka (Using auctionId as the routing key so all bids for one auction go to the same partition)
        BidEvent event = BidEvent.builder().eventType(BidEventType.BID_PLACED).timestamp(Instant.now()).data(BidEventPayload.builder().bidId(bid.getId()).bidderId(bid.getBidderId()).auctionId(bid.getAuctionId()).amount(bid.getAmount()).build()).build();
        bidEventProducer.publish(event);

        log.info("bid_placement_success | auctionId={} | bidId={} | amount={}",  bid.getAuctionId(), bid.getId(), bid.getAmount());
        return BidResponse.builder().id(bid.getId()).auctionId(bid.getAuctionId()).bidderId(bid.getBidderId()).amount(bid.getAmount()).isHighest(true).build();
    }
}