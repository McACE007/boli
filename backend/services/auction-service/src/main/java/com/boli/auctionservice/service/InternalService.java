package com.boli.auctionservice.service;

import com.boli.auctionservice.dto.AuctionRulesResponse;
import com.boli.auctionservice.dto.AuctionWinnerResponse;
import com.boli.common.enums.AuctionStatus;
import com.boli.auctionservice.exception.AuctionNotFoundException;
import com.boli.auctionservice.exception.InvalidAuctionDataException;
import com.boli.auctionservice.mapper.AuctionMapper;
import com.boli.auctionservice.model.Auction;
import com.boli.auctionservice.repository.wrapper.AuctionRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InternalService {
    private final AuctionRepository auctionRepository;
    private final AuctionMapper auctionMapper;

    public AuctionRulesResponse getAuctionRules(Long auctionId){
        log.info("service_get_auction_rules_start | actionId={}", auctionId);

        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> {
            log.warn("auction_fetch_failed | reason=auction_not_found | auctionId={}", auctionId);
            return new AuctionNotFoundException(auctionId);
        });

        if(!auction.getStatus().isBiddable()){
            log.info("service_get_auction_rules_failed | reason=auction_not_live | actionId={} | currentState={}", auctionId, auction.getStatus());
            throw new InvalidAuctionDataException(String.format("Auction %s is not in LIVE state", auctionId));
        }

        log.info("service_get_auction_rules_end | actionId={}", auctionId);

        return auctionMapper.toAuctionRulesResponse(auction);
    }

    public AuctionWinnerResponse getAuctionWinner(Long auctionId){
        log.info("service_get_auction_winner_start | actionId={}", auctionId);

        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> {
            log.warn("auction_fetch_failed | reason=auction_not_found | auctionId={}", auctionId);
            return new AuctionNotFoundException(auctionId);
        });

        if(auction.getStatus() != AuctionStatus.ENDED){
            log.info("service_get_auction_winner_failed | reason=auction_not_ended | actionId={} | currentState={}", auctionId, auction.getStatus());
            throw new InvalidAuctionDataException(String.format("Auction %s is not in ENDED state", auctionId));
        }

        log.info("service_get_auction_winner_end | actionId={} | winnerId={}", auctionId, auction.getWinnerId());

        return auctionMapper.toAuctionWinnerResponse(auction);
    }
}
