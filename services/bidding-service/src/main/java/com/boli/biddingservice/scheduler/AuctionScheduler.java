//package com.boli.biddingservice.scheduler;
//
//import com.boli.auctionservice.service.BidService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AuctionScheduler {
//    private final BidService auctionService;
//
//    @Scheduled(fixedDelay = 60000)
//    public void startAuctions(){
//        auctionService.startAuctions();
//    }
//
//    @Scheduled(fixedDelay = 60000)
//    public void endAuctions(){
//        auctionService.endAuctions();
//    }
//}
