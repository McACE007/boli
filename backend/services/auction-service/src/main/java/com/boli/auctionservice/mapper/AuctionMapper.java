package com.boli.auctionservice.mapper;

import com.boli.auctionservice.dto.AuctionResponse;
import com.boli.auctionservice.dto.AuctionRulesResponse;
import com.boli.auctionservice.dto.AuctionWinnerResponse;
import com.boli.auctionservice.dto.CreateAuctionRequest;
import com.boli.auctionservice.model.Auction;
import com.boli.common.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class AuctionMapper {
    public Auction toAuction(CreateAuctionRequest request) {
        Auction auction = new Auction();
        auction.setTitle(request.getTitle());
        auction.setDescription(request.getDescription());
        auction.setStartingPrice(request.getStartingPrice());
        auction.setMinIncrement(request.getMinIncrement());
        auction.setStartTime(request.getStartTime());
        auction.setEndTime(request.getEndTime());
        return auction;
    }

    public AuctionResponse toAuctionResponse(Auction auction) {
        return AuctionResponse.builder()
                .id(auction.getId()).title(auction.getTitle()).description(auction.getDescription()).startingPrice(auction.getStartingPrice()).minIncrement(auction.getMinIncrement()).startTime(auction.getStartTime()).endTime(auction.getEndTime()).status(auction.getStatus()).sellerId(auction.getSellerId()).createdAt(auction.getCreatedAt()).build();
    }

    public PageResponse<AuctionResponse> toPageResponse(Page<Auction> page) {
        return PageResponse.<AuctionResponse>builder().content(page.getContent().stream().map(this::toAuctionResponse).toList()).page(page.getNumber()).size(page.getSize()).totalElements(page.getTotalElements()).totalPages(page.getTotalPages()).last(page.isLast()).build();
    }

    public AuctionRulesResponse toAuctionRulesResponse(Auction auction) {
        return AuctionRulesResponse.builder().status(auction.getStatus()).startingPrice(auction.getStartingPrice()).minIncrement(auction.getMinIncrement()).startTime(auction.getStartTime()).endTime(auction.getEndTime()).sellerId(auction.getSellerId()).build();
    }

    public AuctionWinnerResponse toAuctionWinnerResponse(Auction auction) {
        return AuctionWinnerResponse.builder().winnerId(auction.getWinnerId()).winningAmount(auction.getWinningAmount()).build();
    }
}
