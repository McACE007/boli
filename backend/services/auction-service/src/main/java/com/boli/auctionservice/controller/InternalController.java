package com.boli.auctionservice.controller;

import com.boli.auctionservice.dto.AuctionRulesResponse;
import com.boli.auctionservice.dto.AuctionWinnerResponse;
import com.boli.auctionservice.service.InternalService;
import com.boli.common.dto.ApiResponse;
import com.boli.common.util.ResponseBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/auctions")
@Slf4j
public class InternalController {
    private final InternalService internalService;

    @GetMapping("/{auctionId}/rules")
    public ResponseEntity<ApiResponse<AuctionRulesResponse>> getAuctionRules(@PathVariable Long auctionId){
        log.info("api_get_auction_rules_called | auctionId={}", auctionId);
        AuctionRulesResponse response = internalService.getAuctionRules(auctionId);
        log.info("api_get_auction_rules_success | auctionId={}", auctionId);
        return ResponseBuilder.success(response, "Auction Rules Fetched Successfully");
    }

    @GetMapping("/{auctionId}/winner")
    public ResponseEntity<ApiResponse<AuctionWinnerResponse>> getAuctionWinner(@PathVariable Long auctionId){
        log.info("api_get_auction_winner_called | auctionId={}", auctionId);
        AuctionWinnerResponse response = internalService.getAuctionWinner(auctionId);
        log.info("api_get_auction_winner_success | auctionId={}", auctionId);
        return ResponseBuilder.success(response,  response.getWinnerId() == null ? "No Bids Placed For This Auction" : "Auction Winner Fetched Successfully");
    }
}
