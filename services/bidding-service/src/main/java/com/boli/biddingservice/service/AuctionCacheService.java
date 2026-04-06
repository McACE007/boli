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
        log.info("auction_cached_in_redis | auctionId={}", auctionId);
    }

    public void markAuctionEnded(Long auctionId){
        String key = AUCTION_KEY_PREFIX + auctionId;
        redisTemplate.opsForHash().put(key, "status", "ENDED");
        log.info("auction_marked_ended_in_redis | auctionId={}", auctionId);
    }
}
