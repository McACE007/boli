package com.boli.biddingservice.kafka;

import com.boli.biddingservice.service.AuctionCacheService;
import com.boli.common.enums.AuctionEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuctionEventListener {
    private final AuctionCacheService cacheService;

    @KafkaListener(topics = "auction-events", groupId = "bidding-service-group")
    public void consume(AuctionEvent event){
        log.info("kafka_event_received | eventType={} | auctionId={}", event.getEventType(), event.getData().getAuctionId());

        if(event.getEventType() == AuctionEventType.AUCTION_STARTED){
            cacheService.cacheActiveAuction(event.getData().getAuctionId(), event.getData().getStartingPrice(), event.getData().getMinIncrement());
            log.info("Bid {} is ready for bids!", event.getData().getAuctionId());
        }else if(event.getEventType() == AuctionEventType.AUCTION_ENDED){
            cacheService.markAuctionEnded(event.getData().getAuctionId());
            log.info("Bid {} is closed for bids!", event.getData().getAuctionId());
        }else{
            log.debug("Ignored event type: {}", event.getEventType());
        }
    }
}
