package com.boli.auctionservice.service;

import com.boli.auctionservice.dto.AuctionFilterRequest;
import com.boli.auctionservice.dto.AuctionResponse;
import com.boli.auctionservice.dto.CreateAuctionRequest;
import com.boli.common.enums.AuctionStatus;
import com.boli.auctionservice.exception.AuctionNotFoundException;
import com.boli.auctionservice.exception.InvalidAuctionDataException;
import com.boli.auctionservice.kafka.AuctionEvent;
import com.boli.auctionservice.kafka.AuctionEventPayload;
import com.boli.auctionservice.kafka.AuctionEventProducer;
import com.boli.auctionservice.mapper.AuctionMapper;
import com.boli.auctionservice.model.Auction;
import com.boli.auctionservice.repository.wrapper.AuctionRepository;
import com.boli.auctionservice.specification.AuctionSpecification;
import com.boli.common.dto.PageResponse;
import com.boli.common.enums.AuctionEventType;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {
  private final AuctionRepository auctionRepository;
  private final AuctionMapper auctionMapper;
  private final AuctionEventProducer auctionEventProducer;

  public AuctionResponse createAuction(CreateAuctionRequest request, Long userId) {
    log.info("auction_creation_initiated | userId={}", userId);
    Auction auction = auctionMapper.toAuction(request);

    if(!auction.getStartTime().isAfter(LocalDateTime.now().plusMinutes(5))){
      log.warn("auction_creation_failed | reason=start_time_too_early | userId={}", userId);
      throw new InvalidAuctionDataException("Start time must be at least 5 minutes from now");
    }

    if(!auction.getEndTime().isAfter(auction.getStartTime().plusMinutes(5))){
      log.warn("auction_creation_failed | reason=end_time_too_early | userId={}", userId);
      throw new InvalidAuctionDataException("End time must be at least 5 minutes from the Start Time");
    }

    auction.setStatus(AuctionStatus.SCHEDULED);
    auction.setSellerId(userId);

    auctionRepository.save(auction);
    log.info("auction_creation_success | auction_id={} | seller_id={}", auction.getId(), auction.getSellerId());

    try {
      auctionEventProducer.publish(AuctionEvent.builder()
              .eventType(AuctionEventType.AUCTION_CREATED)
              .timestamp(LocalDateTime.now())
              .data(AuctionEventPayload.builder()
                      .auctionId(auction.getId())
                      .sellerId(auction.getSellerId())
                      .status(auction.getStatus())
                      .startTime(auction.getStartTime())
                      .endTime(auction.getEndTime())
                      .build())
              .build());
    } catch (Exception e) {
      log.error("auction_event_publish_failed | reason=kafka_unavailable | auctionId={}",
              auction.getId(), e);
    }

    return auctionMapper.toAuctionResponse(auction);
  }

  public AuctionResponse getAuction(Long auctionId) {
    log.info("auction_fetch_initiated | auctionId={}", auctionId);

    Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> {
      log.warn("auction_fetch_failed | reason=auction_not_found | auctionId={}", auctionId);
      return new AuctionNotFoundException(auctionId);
    });

    return auctionMapper.toAuctionResponse(auction);
  }

  public PageResponse<AuctionResponse> getAuctions(AuctionFilterRequest filters) {
    log.info("auctions_fetch_initiated");

    Pageable pageable = PageRequest.of(filters.getPage(), filters.getSize());
    Specification<Auction> specification = Specification.where(AuctionSpecification.hasStatus(filters.getStatus())).and(AuctionSpecification.minPrice(filters.getMinPrice())).and(AuctionSpecification.maxPrice(filters.getMaxPrice()));
    Page<Auction> page = auctionRepository.findAll(specification, pageable);

    log.info("auctions_fetch_success | totalElements={} | page={}" ,page.getTotalElements(), filters.getPage());

    return auctionMapper.toPageResponse(page);
  }

  @Transactional
  public void startAuctions() {
    log.info("scheduler_start_auctions_triggered");

    List<Auction> auctionsToStart = auctionRepository.findAndLockAuctionsToStart(AuctionStatus.SCHEDULED, LocalDateTime.now());
    auctionsToStart.forEach(auction -> {
      if(!auction.getStatus().canTransitionTo(AuctionStatus.LIVE)){
        log.error("auction_transition_skipped | reason=invalid_state_transition | fromState={} | toState={} | auctionId={}", auction.getStatus(), AuctionStatus.LIVE, auction.getId());
        return;
      }
      auction.setStatus(AuctionStatus.LIVE);
      log.info("auction_transitioned_to_live | auctionId={} | sellerId={}",
              auction.getId(), auction.getSellerId());
    });

    auctionRepository.saveAll(auctionsToStart);

    auctionsToStart.forEach(auction -> {
      try {
        auctionEventProducer.publish(AuctionEvent.builder()
                .eventType(AuctionEventType.AUCTION_STARTED)
                .timestamp(LocalDateTime.now())
                .data(AuctionEventPayload.builder()
                        .auctionId(auction.getId())
                        .sellerId(auction.getSellerId())
                        .status(auction.getStatus())
                        .startTime(auction.getStartTime())
                        .endTime(auction.getEndTime())
                        .build())
                .build());
                log.debug("auction_event_publish_success | eventType=AUCTION_STARTED | auctionId={}",
                auction.getId());
      } catch (Exception e) {
        log.error("auction_event_publish_failed | reason=kafka_unavailable | auctionId={}",
                auction.getId(), e);
      }
    });

    log.info("scheduler_start_auctions_complete | auctionsStarted={}", auctionsToStart.size());
  }

  @Transactional
  public void endAuctions() {
    log.info("scheduler_end_auctions_triggered");

    List<Auction> auctionsToEnd = auctionRepository.findAndLockAuctionsToEnd(AuctionStatus.LIVE, LocalDateTime.now());
    auctionsToEnd.forEach(auction -> {
      if(!auction.getStatus().canTransitionTo(AuctionStatus.ENDED)){
        log.error("auction_transition_skipped | reason=invalid_state_transition | fromState={} | toState={} | auctionId={}", auction.getStatus(), AuctionStatus.ENDED, auction.getId());
        return;
      }
      auction.setStatus(AuctionStatus.ENDED);
      log.info("auction_transitioned_to_ended | auctionId={} | sellerId={}",
              auction.getId(), auction.getSellerId());
    });

    auctionRepository.saveAll(auctionsToEnd);

    auctionsToEnd.forEach(auction -> {
      try {
        auctionEventProducer.publish(AuctionEvent.builder()
                .eventType(AuctionEventType.AUCTION_ENDED)
                .timestamp(LocalDateTime.now())
                .data(AuctionEventPayload.builder()
                        .auctionId(auction.getId())
                        .sellerId(auction.getSellerId())
                        .status(auction.getStatus())
                        .startTime(auction.getStartTime())
                        .endTime(auction.getEndTime())
                        .build())
                .build());
        log.debug("auction_event_publish_success | eventType=AUCTION_ENDED | auctionId={}",
                auction.getId());
      } catch (Exception e) {
        log.error("auction_event_publish_failed | reason=kafka_unavailable | auctionId={}",
                auction.getId(), e);
      }
    });

    log.info("scheduler_end_auctions_complete | auctionsEnded={}", auctionsToEnd.size());
  }
}