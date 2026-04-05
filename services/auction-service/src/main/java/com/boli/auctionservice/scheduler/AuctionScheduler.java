package com.boli.auctionservice.scheduler;

import com.boli.auctionservice.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionScheduler {
    private final AuctionService auctionService;

    @Scheduled(fixedDelay = 60000)
    public void startAuctions(){
        return auctionService.start
    }
}
