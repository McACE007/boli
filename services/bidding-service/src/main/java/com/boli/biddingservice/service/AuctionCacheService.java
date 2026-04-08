package com.boli.biddingservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionCacheService {
    private final StringRedisTemplate redisTemplate;
    private static final String AUCTION_KEY_PREFIX = "auction:";

    public void cacheActiveAuction(Long auctionId, Double startingPrice, Double minIncrement){
        String key = AUCTION_KEY_PREFIX + auctionId;
        Map<String, String> auctionData = Map.of("status", "LIVE", "startingPrice", String.valueOf(startingPrice), "minIncrement", String.valueOf(minIncrement));
        redisTemplate.opsForHash().putAll(key, auctionData);
        log.info("cache_auction_save_success | auctionId={}", auctionId);
    }

    public void markAuctionEnded(Long auctionId){
        String key = AUCTION_KEY_PREFIX + auctionId;
        redisTemplate.opsForHash().put(key, "status", "ENDED");
        log.info("cache_auction_end_success | auctionId={}", auctionId);
    }

    public Map<Object, Object> getAuctionMetadata(Long auctionId){
        String key = AUCTION_KEY_PREFIX + auctionId;
        Map<Object, Object> result = redisTemplate.opsForHash().entries(key);
        log.debug("cache_auction_fetch_success | auctionId={}", auctionId);
        return result;
    }

    public void updateHighestBid(Long auctionId, Long bidderId, Double amount){
        String key = AUCTION_KEY_PREFIX + auctionId;
        Map<String, String> updates = Map.of("highestBid", String.valueOf(amount), "highestBidderId", String.valueOf(bidderId));
        redisTemplate.opsForHash().putAll(key, updates);
        log.debug("cache_highest_bid_update_success | auctionId={} | bidderId={}", auctionId, bidderId);
    }
}
