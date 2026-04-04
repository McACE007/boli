package com.boli.auctionservice.service;

import com.boli.auctionservice.dto.AuctionResponse;
import com.boli.auctionservice.dto.CreateAuctionRequest;
import com.boli.auctionservice.enums.AuctionStatus;
import com.boli.auctionservice.exception.InvalidAuctionDataException;
import com.boli.auctionservice.mapper.AuctionMapper;
import com.boli.auctionservice.model.Auction;
import com.boli.auctionservice.repository.wrapper.AuctionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {
  private final AuctionRepository auctionRepository;
  private final AuctionMapper auctionMapper;

  public AuctionResponse createAuction(CreateAuctionRequest request, Long userId) {
    log.info("auction_creation_initiated | userId={}", userId);
    Auction auction = auctionMapper.toAuction(request);

    if(!auction.getStartTime().isAfter(LocalDateTime.now().plusMinutes(5))){
      log.error("auction_creation_failed | reason=Start time must be at least 5 minutes from now | userId={}", userId);
      throw new InvalidAuctionDataException("Start time must be at least 5 minutes from now");
    }

    if(!auction.getEndTime().isAfter(auction.getStartTime().plusMinutes(5))){
      log.error("auction_creation_failed | reason=End time must be at least 5 minutes from the Start Time | userId={}", userId);
      throw new InvalidAuctionDataException("End time must be at least 5 minutes from the Start Time");
    }

    auction.setStatus(AuctionStatus.CREATED);
    auction.setSellerId(userId);

    try {
      auctionRepository.save(auction);
      log.info("auction_creation_success | auction_id={} | seller_id={}", auction.getId(), auction.getSellerId());
    } catch (Exception e) {
      log.error(
              "auction_creation_failed_unexpected | auction_id={} | seller_id={}",
              auction.getId(), auction.getSellerId(),
              e);
      throw e;
    }
    return auctionMapper.toAuctionResponse(auction);
  }
}