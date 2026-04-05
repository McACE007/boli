package com.boli.auctionservice.controller;

import com.boli.auctionservice.dto.AuctionFilterRequest;
import com.boli.auctionservice.dto.AuctionResponse;
import com.boli.auctionservice.dto.CreateAuctionRequest;
import com.boli.auctionservice.service.AuctionService;
import com.boli.common.dto.ApiResponse;
import com.boli.common.dto.PageResponse;
import com.boli.common.util.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auctions")
@Slf4j
@RequiredArgsConstructor
public class AuctionController {
  private final AuctionService auctionService;

  @PostMapping
  public ResponseEntity<ApiResponse<AuctionResponse>> createAuction(Authentication authentication, @RequestBody @Valid CreateAuctionRequest request) {
    Long userId = (Long) authentication.getPrincipal();
    log.info("api_create_auction_initiated | userId={}", userId);
    AuctionResponse auctionResponse = auctionService.createAuction(request, userId);
    log.info("api_create_auction_success | userId={}", userId);
    return ResponseBuilder.created(auctionResponse, "Auction Created Successfully");
  }

  @GetMapping("/{auctionId}")
  public ResponseEntity<ApiResponse<AuctionResponse>> getAuction(@PathVariable Long auctionId) {
    log.info("api_get_auction_initiated | auctionId={}", auctionId);
    AuctionResponse auctionResponse = auctionService.getAuction(auctionId);
    log.info("api_get_auction_success | auctionId={}", auctionId);
    return ResponseBuilder.success(auctionResponse, "Auction Fetched Successfully");
  }

  @GetMapping
  public ResponseEntity<ApiResponse<PageResponse<AuctionResponse>>> getAuctions(@ModelAttribute AuctionFilterRequest auctionFilterRequest) {
    log.info("api_get_auctions_initiated");
    PageResponse<AuctionResponse> pageResponse = auctionService.getAuctions(auctionFilterRequest);
    log.info("api_get_auctions_success");
    return ResponseBuilder.success(pageResponse, "Auctions Fetched Successfully");
  }
}
