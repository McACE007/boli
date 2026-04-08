package com.boli.biddingservice.controller;

import com.boli.biddingservice.dto.BidRequest;
import com.boli.biddingservice.dto.BidResponse;
import com.boli.biddingservice.service.BidService;
import com.boli.common.dto.ApiResponse;
import com.boli.common.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bids")
@Slf4j
@RequiredArgsConstructor
public class BidController {
    private final BidService service;

    @PostMapping
    public ResponseEntity<ApiResponse<BidResponse>> placeBid(Authentication authentication, @RequestBody @Valid BidRequest request){
        Long bidderId = (Long) authentication.getPrincipal();
        log.info("api_bid_placement_initiated | auctionId={} | bidderId={}", request.getAuctionId(), bidderId);
        BidResponse response = service.placeBid(request, bidderId);
        log.info("api_place_bid_success | auctionId={} | bidderId={} | bidId={}", request.getAuctionId(), bidderId, response.getId());
        return ResponseBuilder.created(response, "Bid Placed Successfully");
    }
}
