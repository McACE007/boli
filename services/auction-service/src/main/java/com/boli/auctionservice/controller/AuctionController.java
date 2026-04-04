package com.boli.auctionservice.controller;

import com.boli.auctionservice.dto.AuctionResponse;
import com.boli.auctionservice.dto.CreateAuctionRequest;
import com.boli.auctionservice.service.AuctionService;
import com.boli.common.dto.ApiResponse;
import com.boli.common.util.ResponseBuilder;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auctions")
@Slf4j
@RequiredArgsConstructor
public class AuctionController {
  private final AuctionService auctionService;

  @PostMapping("/")
  public ResponseEntity<ApiResponse<AuctionResponse>> createAuction(Authentication authentication, @RequestBody @Valid CreateAuctionRequest request) {
    Long userId = (Long) authentication.getPrincipal();
    log.info("api_create_auction_initiated | userId={}", userId);
    AuctionResponse auctionResponse = auctionService.createAuction(request, userId);
    log.info("api_create_auction_success | userId={}", userId);
    return ResponseBuilder.created(auctionResponse, "Auction Created Successfully");
  }
}
